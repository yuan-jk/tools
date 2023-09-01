package cn.com.yuanjk.jts;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.util.LineStringExtracter;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import java.util.Collection;
import java.util.List;

public class OverlayFunctions {


    public static void main(String[] args) throws ParseException {
        String lineWkt = "LineString (125.790 44.172, 125.418 43.782, 125.618 43.620, 125.395 43.439, 125.586 43.286, 125.369 43.189, 125.620 43.026, 125.262 42.875)";
//        String lineWkt = "LINESTRING (125.5 42, 125.5 45)";
        String polygonWkt = "Polygon ((125 44, 126 44, 126 43, 125 43, 125 44))";

        WKTReader wktReader = new WKTReader();
//
        Geometry line = wktReader.read(lineWkt);
        Geometry polygon = wktReader.read(polygonWkt);
//
//        Geometry rs = polygon.difference(line);
//
//        System.out.println(rs.getGeometryType());
//
//        WKTWriter wktWriter = new WKTWriter();
//        System.out.println(wktWriter.write(rs));
//        System.out.println(wktWriter.write(line.difference(polygon)));
//        System.out.println(wktWriter.write(polygon.union(line)));
//
//        line.touches(polygon);
//        System.out.println(rs.getGeometryType());
//        System.out.println(rs.getGeometryType());
        polygonSplit(((Polygon) polygon), ((LineString) line));
    }


    public static void polygonSplit(Polygon geom) {
        Envelope envelope = geom.getEnvelopeInternal();

        GeometryFactory geometryFactory = new GeometryFactory();
        double x = (envelope.getMinX() + envelope.getMaxX()) / 2;
        Coordinate[] coordinates = {new Coordinate(x, envelope.getMinY()), new Coordinate(x, envelope.getMaxY())};
        LineString line = geometryFactory.createLineString(coordinates);

        Geometry nodedLinework = geom.getBoundary().union(line);
        List lines = LineStringExtracter.getLines(nodedLinework);
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(lines);
        Collection polys = polygonizer.getPolygons();
        Polygon[] polyArray = GeometryFactory.toPolygonArray(polys);
        GeometryCollection geometryCollection = geometryFactory.createGeometryCollection(polyArray);
        WKTWriter wktWriter = new WKTWriter();
        System.out.println("==" + wktWriter.write(geometryCollection));
    }

    public static Polygon[] polygonSplit(Polygon geom, LineString line) {
//        GeometryFactory geometryFactory = geom.getFactory();
        WKTWriter wktWriter = new WKTWriter();
//        System.out.println("[polygon] " + wktWriter.write(geom));
//        System.out.println("[linestring] " + wktWriter.write(line));
        Geometry boundary = geom.getBoundary();
//        System.out.println("[boundary] " + wktWriter.write(boundary));
        Geometry nodedLinework = geom.getBoundary().union(line);
        List lines = LineStringExtracter.getLines(nodedLinework);
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(lines);
        Collection polys = polygonizer.getPolygons();
        return GeometryFactory.toPolygonArray(polys);
    }


}
