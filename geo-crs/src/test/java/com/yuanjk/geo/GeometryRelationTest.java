package com.yuanjk.geo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

/**
 * @author yuanjk
 * @version 21/3/30
 */
public class GeometryRelationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void polygonCrsTransform() throws ParseException {
        String wktMutiPolygon = "MultiPolygon (((106.70206058681142736 29.57404409748362184, 106.70640989759029083 " +
                "29.57283633583158533, 106.70015424526575032 29.57061580126033462, 106.70015424526575032 " +
                "29.57061580126033462, 106.70206058681142736 29.57404409748362184)))";

        GeometryRelation.readGeometryFromWkt(wktMutiPolygon);
    }


    @Test
    public void relationTest() throws ParseException {
        String pointWkt1 = "POINT (1 1)";

        String lineWkt = "LINESTRING(0 1, 3 1)";

        Geometry point1 = GeometryRelation.readGeometryFromWkt(pointWkt1);
        Geometry line = GeometryRelation.readGeometryFromWkt(lineWkt);

        System.out.println("point1 and line: " + point1.relate(line));
        System.out.println("point1 cross line: " + point1.crosses(line));
        System.out.println("point1 within line: " + point1.within(line));

        System.out.println("line and point1: " + line.relate(point1));
        System.out.println("line cross point1: " + line.crosses(point1));
        System.out.println("line within point1: " + line.within(point1));

        String pointWkt2 = "POINT (0 1)";
        Geometry point2 = GeometryRelation.readGeometryFromWkt(pointWkt2);
        System.out.println("point2 and line: " + point2.relate(line));
        System.out.println("point2 within line: " + point2.within(line));
//        System.out.println("point1 cross line: " + point1.crosses(line));

        String pointWkt3 = "POINT (0 0)";
        Geometry point3 = GeometryRelation.readGeometryFromWkt(pointWkt3);
        System.out.println("point3 and line: " + point3.relate(line));
        System.out.println("point3 within line: " + point3.within(line));


        System.out.println("point within point: " + point1.within(point1));
        System.out.println("line within line: " + line.within(line));

    }


    @Test
    public void lineAndPolygonWithinTest() throws ParseException {
        String pointWkt1 = "POINT (1 1)";

        String lineWkt1 = "LINESTRING(0 1, 3 1)";

        String polygonWkt = "POLYGON((0 0, 0 1, 2 1, 2 0, 0 0))";
        Geometry polygon = GeometryRelation.readGeometryFromWkt(polygonWkt);
        Geometry line1 = GeometryRelation.readGeometryFromWkt(lineWkt1);

        System.out.println("line1 relate polygon: " + line1.relate(polygon));
        System.out.println("line1 touch polygon: " + line1.touches(polygon));
        System.out.println("line1 cross polygon: " + line1.crosses(polygon));
        System.out.println("line1 within polygon: " + line1.within(polygon));
        System.out.println("line1 overlap polygon: " + line1.overlaps(polygon));



        String lineWkt2 = "LINESTRING(0 1, 1 1)";

        Geometry line2 = GeometryRelation.readGeometryFromWkt(lineWkt2);

        System.out.println("line2 relate polygon: " + line2.relate(polygon));
        System.out.println("line2 touch polygon: " + line2.touches(polygon));
        System.out.println("line2 cross polygon: " + line2.crosses(polygon));
        System.out.println("line2 within polygon: " + line2.within(polygon));
        System.out.println("line2 overlap polygon: " + line2.overlaps(polygon));

    }


    @Test
    public void polygonAndPolygonCrossTest() throws ParseException {
        String polygonWkt = "POLYGON((0 0, 0 1, 2 1, 2 0, 0 0))";
        String polygonWkt2 = "POLYGON((0 0, 0 2, 2 2, 1 0, 0 0))";
        Geometry polygon = GeometryRelation.readGeometryFromWkt(polygonWkt);
        Geometry polygon2 = GeometryRelation.readGeometryFromWkt(polygonWkt2);

        System.out.println("polygon2 relate polygon: " + polygon2.relate(polygon));
        System.out.println("polygon2 touch polygon: " + polygon2.touches(polygon));
        System.out.println("polygon2 cross polygon: " + polygon2.crosses(polygon));
        System.out.println("polygon2 within polygon: " + polygon2.within(polygon));
        System.out.println("polygon2 overlap polygon: " + polygon2.overlaps(polygon));


        String lineWkt2 = "LINESTRING(0 1, 1 1)";

        Geometry line2 = GeometryRelation.readGeometryFromWkt(lineWkt2);

        System.out.println("line2 relate polygon: " + line2.relate(polygon));
        System.out.println("line2 touch polygon: " + line2.touches(polygon));
        System.out.println("line2 cross polygon: " + line2.crosses(polygon));
        System.out.println("line2 within polygon: " + line2.within(polygon));
        System.out.println("line2 overlap polygon: " + line2.overlaps(polygon));
    }
}