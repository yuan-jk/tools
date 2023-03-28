package com.yuanjk.arrow;


import org.apache.arrow.flight.*;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.util.AutoCloseables;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorLoader;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.VectorUnloader;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FlightServerAndClient {

    FlightServer flightServer;

    class Dataset implements AutoCloseable {
        private final List<ArrowRecordBatch> batches;
        private final Schema schema;
        private final long rows;

        public Dataset(List<ArrowRecordBatch> batches, Schema schema, long rows) {
            this.batches = batches;
            this.schema = schema;
            this.rows = rows;
        }

        public List<ArrowRecordBatch> getBatches() {
            return batches;
        }

        public Schema getSchema() {
            return schema;
        }

        public long getRows() {
            return rows;
        }

        @Override
        public void close() throws Exception {
            AutoCloseables.close(batches);
        }
    }

    class CookbookProducer extends NoOpFlightProducer implements AutoCloseable {
        private final BufferAllocator allocator;
        private final Location location;
        private final ConcurrentMap<FlightDescriptor, Dataset> datasets;

        public CookbookProducer(BufferAllocator allocator, Location location) {
            this.allocator = allocator;
            this.location = location;
            this.datasets = new ConcurrentHashMap<>();
        }

        @Override
        public Runnable acceptPut(CallContext context, FlightStream flightStream, StreamListener<PutResult> ackStream) {
            List<ArrowRecordBatch> batches = new ArrayList<>();
            return () -> {
                long rows = 0;
                VectorUnloader unloader;
                while (flightStream.next()) {
                    unloader = new VectorUnloader(flightStream.getRoot());
                    final ArrowRecordBatch arb = unloader.getRecordBatch();
                    batches.add(arb);
                    rows += flightStream.getRoot().getRowCount();
                }
                Dataset dataset = new Dataset(batches, flightStream.getSchema(), rows);
                datasets.put(flightStream.getDescriptor(), dataset);
                ackStream.onCompleted();
            };
        }

        @Override
        public void getStream(CallContext context, Ticket ticket, ServerStreamListener listener) {
            FlightDescriptor flightDescriptor = FlightDescriptor.path(
                    new String(ticket.getBytes(), StandardCharsets.UTF_8));
            Dataset dataset = this.datasets.get(flightDescriptor);
            if (dataset == null) {
                throw CallStatus.NOT_FOUND.withDescription("Unknown descriptor").toRuntimeException();
            }
            try (VectorSchemaRoot root = VectorSchemaRoot.create(
                    this.datasets.get(flightDescriptor).getSchema(), allocator)) {
                VectorLoader loader = new VectorLoader(root);
                listener.start(root);
                for (ArrowRecordBatch arrowRecordBatch : this.datasets.get(flightDescriptor).getBatches()) {
                    loader.load(arrowRecordBatch);
                    listener.putNext();
                }
                listener.completed();
            }
        }

        @Override
        public void doAction(CallContext context, Action action, StreamListener<Result> listener) {
            FlightDescriptor flightDescriptor = FlightDescriptor.path(
                    new String(action.getBody(), StandardCharsets.UTF_8));
            switch (action.getType()) {
                case "DELETE": {
                    Dataset removed = datasets.remove(flightDescriptor);
                    if (removed != null) {
                        try {
                            removed.close();
                        } catch (Exception e) {
                            listener.onError(CallStatus.INTERNAL
                                    .withDescription(e.toString())
                                    .toRuntimeException());
                            return;
                        }
                        Result result = new Result("Delete completed".getBytes(StandardCharsets.UTF_8));
                        listener.onNext(result);
                    } else {
                        Result result = new Result("Delete not completed. Reason: Key did not exist."
                                .getBytes(StandardCharsets.UTF_8));
                        listener.onNext(result);
                    }
                    listener.onCompleted();
                }
            }
        }

        @Override
        public FlightInfo getFlightInfo(CallContext context, FlightDescriptor descriptor) {
            FlightEndpoint flightEndpoint = new FlightEndpoint(
                    new Ticket(descriptor.getPath().get(0).getBytes(StandardCharsets.UTF_8)), location);
            return new FlightInfo(
                    datasets.get(descriptor).getSchema(),
                    descriptor,
                    Collections.singletonList(flightEndpoint),
                    /*bytes=*/-1,
                    datasets.get(descriptor).getRows()
            );
        }

        @Override
        public void listFlights(CallContext context, Criteria criteria, StreamListener<FlightInfo> listener) {
            datasets.forEach((k, v) -> {
                listener.onNext(getFlightInfo(null, k));
            });
            listener.onCompleted();
        }

        @Override
        public void close() throws Exception {
            AutoCloseables.close(datasets.values());
        }
    }

    public void run(String serverHost) {
        Location location = Location.forGrpcInsecure(serverHost, 33333);
        try (BufferAllocator allocator = new RootAllocator()) {
            try (final CookbookProducer producer = new CookbookProducer(allocator, location);
//                 final FlightServer flightServer = FlightServer.builder(allocator, location, producer).build()) {
            ) {
                this.flightServer = FlightServer.builder(allocator, location, producer).build();
                try {
                    this.flightServer.start();
                    System.out.println("S1: Server (Location): Listening on port " + flightServer.getPort());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void populateData(FlightClient flightClient, BufferAllocator allocator) throws InterruptedException {
//        try (FlightClient flightClient = FlightClient.builder(allocator, location).build()) {
//        System.out.println("C1: Client (Location): Connected to " + location.getUri());

        // Populate data
        Schema schema = new Schema(
                Collections.singletonList(new Field("name", FieldType.nullable(new ArrowType.Utf8()), null)));
        try (VectorSchemaRoot vectorSchemaRoot = VectorSchemaRoot.create(schema, allocator);
             VarCharVector varCharVector = (VarCharVector) vectorSchemaRoot.getVector("name")) {
            varCharVector.allocateNew(3);
            varCharVector.set(0, "Ronald".getBytes());
            varCharVector.set(1, "David".getBytes());
            varCharVector.set(2, "Francisco".getBytes());
            vectorSchemaRoot.setRowCount(3);
            FlightClient.ClientStreamListener listener = flightClient.startPut(FlightDescriptor.path("profiles"),
                    vectorSchemaRoot, new AsyncPutListener());
            listener.putNext();
            varCharVector.set(0, "Manuel".getBytes());
            varCharVector.set(1, "Felipe".getBytes());
            varCharVector.set(2, "JJ".getBytes());
            vectorSchemaRoot.setRowCount(3);
            listener.putNext();
            listener.completed();
            listener.getResult();
            System.out.println("C2: Client (Populate Data): Wrote 2 batches with 3 rows each");
        }
    }

    public Iterable<FlightInfo> getAllFlightInfo(FlightClient flightClient) {
        Iterable<FlightInfo> flightInfosBefore = flightClient.listFlights(Criteria.ALL);
        System.out.print("C5: Client (List Flights Info): ");
        flightInfosBefore.forEach(System.out::println);
        return flightInfosBefore;
    }

    public FlightInfo getFlightInfo(FlightClient flightClient, String name) {
        FlightInfo flightInfo = flightClient.getInfo(FlightDescriptor.path(name));
        System.out.println("C3: Client (Get Metadata): " + flightInfo);
        return flightInfo;
    }

    public void getData(FlightClient flightClient) {
        try (FlightStream flightStream = flightClient.getStream(new Ticket(
                FlightDescriptor.path("profiles").getPath().get(0).getBytes(StandardCharsets.UTF_8)))) {
            int batch = 0;
            try (VectorSchemaRoot vectorSchemaRootReceived = flightStream.getRoot()) {
                System.out.println("C4: Client (Get Stream):");
                while (flightStream.next()) {
                    batch++;
                    System.out.println("Client Received batch #" + batch + ", Data:");
                    System.out.print(vectorSchemaRootReceived.contentToTSVString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDataSet(FlightClient flightClient) {
        Iterator<Result> deleteActionResult = flightClient.doAction(new Action("DELETE",
                FlightDescriptor.path("profiles").getPath().get(0).getBytes(StandardCharsets.UTF_8)));
        while (deleteActionResult.hasNext()) {
            Result result = deleteActionResult.next();
            System.out.println("C6: Client (Do Delete Action): " +
                    new String(result.getBody(), StandardCharsets.UTF_8));
        }

    }

}