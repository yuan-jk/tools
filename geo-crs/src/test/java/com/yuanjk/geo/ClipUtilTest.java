package com.yuanjk.geo;

import com.yuanjk.geo.shp.ShpReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * 类<code>Doc</code>用于：TODO
 *
 * @author yuanjk
 * @version 1.0
 */
public class ClipUtilTest {

    private static final WKTReader reader = new WKTReader();
    private static final Polygon polygon;

    private static final LineString lineString;

    private static final Point point;

    static {
        try {
//            polygon = ((Polygon) reader.read("Polygon ((28.46394154284550382 25.38091211886831999, " +
//                    "43.6370078729900257 28.89966598819459875, 44.66019162222674055 4.19352179930796609, " +
//                    "28.01473892122938381 3.17033805007124414, 28.46394154284550382 25.38091211886831999))"));


            polygon = ((Polygon) reader.read("Polygon ((4.4666779601878801 -4.4139520622370938, " +
                    "43.45052765391263705 -16.66335764102993977, 6.00367561836340968 -19.8770800172151354, " +
                    "4.4666779601878801 -4.4139520622370938))"));

            lineString = ((LineString) reader.read(""));

            point = ((Point) reader.read(""));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void polygonAndPolygon() throws IOException, URISyntaxException {
        WKTWriter writer = new WKTWriter();
        String polygonFile =
                Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("shp/clip/Polygons.shp")).toURI()).toFile().getAbsolutePath();
        List<Geometry> geometries = ShpReader.readAsGeometry(polygonFile);

        for (Geometry geometry : geometries) {
            Geometry intersection = polygon.intersection(geometry);
            Object userData = intersection.getUserData();
            if (userData != null) {
                ((Map<String, Object>) userData).forEach((k, v) -> System.out.println(k + "=" + v));
            } else {
                System.out.println("no user data");
            }
            System.out.println(writer.write(intersection));
        }

    }

    @Test
    public void polygonAndStringLine() throws IOException, URISyntaxException {
        WKTWriter writer = new WKTWriter();
        String polygonFile =
                Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("shp/clip/StringLines.shp")).toURI()).toFile().getAbsolutePath();
        List<Geometry> geometries = ShpReader.readAsGeometry(polygonFile);

        for (Geometry geometry : geometries) {
            polygon.getGeometryType();
            Geometry intersection = polygon.intersection(geometry);
            Map<String, Object> usd = ((Map<String, Object>) geometry.getUserData());
            System.out.println(usd.get("id"));
            Object userData = intersection.getUserData();
            if (userData != null) {
                ((Map<String, Object>) userData).forEach((k, v) -> System.out.println(k + "=" + v));
            } else {
                System.out.println("no user data");
            }
            System.out.println(writer.write(intersection));
        }

    }

}