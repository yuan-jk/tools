package cn.com.yuanjk.jts;

import org.locationtech.jts.geom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DistributedDifference extends RecursiveTask<List<Polygon>> {
    public static final Logger log = LoggerFactory.getLogger(DistributedDifference.class);
    public static final int SIZE_THRESHOLD = 1000;

    private Polygon baseGeom;
    private List<Polygon> eraseGeomList;

    public DistributedDifference() {
        super();
    }

    public DistributedDifference(Polygon baseGeom, List<Polygon> eraseGeomList) {
        super();
        this.baseGeom = baseGeom;
        this.eraseGeomList = eraseGeomList;
    }

    @Override
    protected List<Polygon> compute() {
        if (eraseGeomList.size() <= SIZE_THRESHOLD) {
            return removeRoadBuffer(baseGeom, eraseGeomList);
        } else {
            log.info("task is split: [{}]", eraseGeomList.size());
            Envelope envelope = baseGeom.getEnvelopeInternal();
            GeometryFactory geometryFactory = baseGeom.getFactory();
            double width = envelope.getWidth(), height = envelope.getHeight();
            Coordinate[] coordinates = new Coordinate[2];
            if (width >= height) {
                coordinates[0] = new Coordinate(envelope.getMinX() + width / 2, envelope.getMinY());
                coordinates[1] = new Coordinate(envelope.getMinX() + width / 2, envelope.getMaxY());
            } else {
                coordinates[0] = new Coordinate(envelope.getMinX(), envelope.getMinY() + height / 2);
                coordinates[1] = new Coordinate(envelope.getMaxX(), envelope.getMinY() + height / 2);
            }
            LineString lineString = geometryFactory.createLineString(coordinates);

            Polygon[] polygons = OverlayFunctions.polygonSplit(baseGeom, lineString);

            if (polygons.length == 2) {
                DistributedDifference task1 = new DistributedDifference(polygons[0],
                        filterPolygon(polygons[0], eraseGeomList));
                DistributedDifference task2 = new DistributedDifference(polygons[1],
                        filterPolygon(polygons[1], eraseGeomList));

                invokeAll(task1, task2);
                List<Polygon> result1 = task1.join();
                List<Polygon> result2 = task2.join();

                return unionTouchedPolygon(result1, result2);
            } else {
                return Collections.EMPTY_LIST;
            }
        }
    }

    public List<Polygon> removeRoadBuffer(Polygon polygon, List<Polygon> differencePolygons) {
        List<Polygon> basePolygons = Collections.singletonList(polygon);
        for (Polygon differencePolygon : differencePolygons) {
            List<Polygon> tmpPolygons = new ArrayList<>();
            for (Polygon basePolygon : basePolygons) {
                Geometry diff = basePolygon.difference(differencePolygon);
                if (diff instanceof GeometryCollection) {
                    for (int i = 0; i < diff.getNumGeometries(); i++) {
                        Geometry diffSub = diff.getGeometryN(i);
                        if (diffSub instanceof Polygon) {
                            tmpPolygons.add(((Polygon) diffSub));
//                            log.info("-.-");
                        } else {
                            log.info("difference result is omitted, type is [{}]", diffSub.getGeometryType());
                        }
                    }
                } else if (diff instanceof Polygon) {
                    tmpPolygons.add(((Polygon) diff));
//                    log.info("-.-.-");
                } else {
                    log.info("difference result is omitted, type is [{}]", diff.getGeometryType());
                }
            }
            basePolygons = tmpPolygons;
        }
        return basePolygons;
    }


    public List<Polygon> filterPolygon(Polygon polygon, List<Polygon> diffPolygons) {
        List<Polygon> basePolygons = new ArrayList<>();
        for (Polygon diffPolygon : diffPolygons) {
            if (polygon.getEnvelopeInternal().intersects(diffPolygon.getEnvelopeInternal())) {
                basePolygons.add(diffPolygon);
            }
        }
        return basePolygons;
    }

    public List<Polygon> unionTouchedPolygon(List<Polygon> list1, List<Polygon> list2) {
        list1.addAll(list2);
        GeometryCollection geometryCollection = new GeometryCollection(list1.toArray(new Polygon[0]),
                new GeometryFactory());

        Geometry geometry = geometryCollection.union();
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            Geometry geom = geometry.getGeometryN(i);
            if (geom instanceof Polygon) {
                polygons.add(((Polygon) geom));
            } else if (geom instanceof MultiPolygon) {
                MultiPolygon multiPolygon = ((MultiPolygon) geom);
                for (int m = 0; m < multiPolygon.getNumGeometries(); m++) {
                    polygons.add(((Polygon) multiPolygon.getGeometryN(m)));
                }
            }
        }

        return polygons;
    }


}
