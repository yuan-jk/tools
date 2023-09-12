package cn.com.yuanjk.file;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.schema.GroupType;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParquetReaderY {


    public static void main(String[] args) throws IOException {
        String parFile = "D:\\data\\spatial_data\\美国\\纽约出租车\\DATA\\green_tripdata_2023-05.parquet";
        ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(
                new Path(Paths.get(parFile).toUri())).build();

//        java.nio.file.Path path = Paths.get(parFile);
//        reader = AvroParquetReader.<GenericRecord>builder(
//                HadoopInputFile.fromPath(new Path(path.toUri()), null)).build();
        String outputFile = "D:\\data\\spatial_data\\美国\\纽约出租车\\DATA\\green_tripdata_2023-05-v2.csv";

        CSVPrinter csvPrinter = new CSVPrinter(new PrintStream(outputFile), CSVFormat.DEFAULT);

        List<String> fieldNames = new ArrayList<>();
        boolean isFirst = true;
        GenericRecord nextRecord = reader.read();

        while (nextRecord != null) {
            if (isFirst) {
                Schema schema = nextRecord.getSchema();
                schema.getFields().forEach(field -> {
                    System.out.println(field.name());
                    fieldNames.add(field.name());
                });
                isFirst = false;

                csvPrinter.printRecord(fieldNames);
            }

            for (String field : fieldNames) {
                csvPrinter.print(nextRecord.get(field));
            }
            csvPrinter.println();
            nextRecord = reader.read();
        }
    }

    private static void readParquet(String filePath) throws IOException {
        Path file = new Path(filePath);
        ParquetReader.Builder<Group> builder = ParquetReader.builder(new GroupReadSupport(), file);
        ParquetReader<Group> reader = builder.build();
        SimpleGroup group = null;
        GroupType groupType = null;
        int count = 0;
        while ((group = (SimpleGroup) reader.read()) != null) {
            if (groupType == null) {
                groupType = group.getType();
            }
            if (count < 100) {
                Map<String, Object> resultMap = new LinkedHashMap<>();
                for (int i = 0; i < groupType.getFieldCount(); i++) {
                    String tmpName = groupType.getFieldName(i);
                    try {
                        String tmp = group.getValueToString(i, 0);
                        System.out.println(tmpName + ":" + tmp);
                    } catch (Exception e) {
                        System.out.println(tmpName + ":" + "");
                    }
                }
                count++;
            }
        }
    }

}
