package com.yuanjk.geo;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTWriter2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.opengis.referencing.crs.CRSAuthorityFactory;

import static org.junit.Assert.*;

public class CrsTransformTest {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void defineCrs() {
    }

    @Test
    public void wktCrs() {
    }

    @Test
    public void crsToWkt() throws FactoryException {

        String epsgCode1 = "EPSG:4326";
        CrsTransform.crsToWkt(epsgCode1);

        String epsgCode2 = "EPSG:4490";
        CrsTransform.crsToWkt(epsgCode2);

        String epsgCode3 = "EPSG:4545";
        CrsTransform.crsToWkt(epsgCode3);
//        String epsgCode1 = "EPSG:4326";

    }

    @Test
    public void authorityTest() throws FactoryException {
        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);

        factory.getAuthority().getIdentifiers().forEach(i -> System.out.println(i));

//        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("4326");
    }


    @Test
    public void crsToWktFormat() {
    }

    @Test
    public void matchCrs() {
    }

    @Test
    public void findMathTransform() throws ParseException, FactoryException, TransformException {

//        String wkt = "POINT (106.711991 29.562189)";
        String wkt = "POINT (29.562189 106.711991)";
        String sourceCrs = "epsg:4490";
        String targetCrs = "epsg:4545";

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        WKTReader reader = new WKTReader(geometryFactory);

        Geometry geometry = reader.read(wkt);

        CoordinateReferenceSystem sourceCRS = CRS.decode(sourceCrs.trim().toUpperCase());
        CoordinateReferenceSystem targetCRS = CRS.decode(targetCrs.trim().toUpperCase());

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
        WKTWriter2 wktWriter2 = new WKTWriter2();
        System.out.println(wktWriter2.write(JTS.transform(geometry, transform)));
    }

    @Test
    public void transformGeometry() {

        Map<String, Integer> map = new HashMap<>();

        System.out.println(1 << 3);
        System.out.println(1 << 6);
        System.out.println(1 >> 1);
        System.out.println(1 >> 3);


        System.out.println("0b001->" + (0b001));
        System.out.println("0b011->" + (0b011));
        System.out.println("0b100->" + (0b100));

        System.out.println("2=>" + (Integer.toBinaryString(2)));
        System.out.println("2 >> 3=>" + (Integer.toBinaryString(2 >> 3)));
        System.out.println("2<< 3=>" + (Integer.toBinaryString(2 << 3)));

    }

    @Test
    public void axisOrder() {


    }

    @Test
    public void tableSizeForTest() {
        System.out.println("3 -> " + tableSizeFor(3));
        System.out.println("5 -> " + tableSizeFor(5));
        System.out.println("32 -> " + tableSizeFor(32));
        System.out.println("300 -> " + tableSizeFor(300));
        System.out.println("4096 -> " + tableSizeFor(4096));
        System.out.println("4097 -> " + tableSizeFor(4097));
        System.out.println("2^29 " + (2 << 16));
        System.out.println("2^29 + 3 " + tableSizeFor((2 << 16) + 3));
        System.out.println("2^29 + 3 " + tableSizeFor((2 << 16) - 3));
        System.out.println("2^29 :" + ((2 << 26)));
        System.out.println("2^29 :" + tableSizeFor((2 << 26) - 3));
        System.out.println("2^29 :" + tableSizeFor((2 << 26) + 3));

    }

    @Test
    public void printTable() {
        System.out.println("5000 >>> 0: " + (Integer.toBinaryString(5000)));
        System.out.println("5000 >>> 1: " + (Integer.toBinaryString(5000 >>> 1)));
        System.out.println("5000 >>> 2: " + (Integer.toBinaryString(5000 >>> 2)));
        System.out.println("5000 >>> 3: " + (Integer.toBinaryString(5000 >>> 3)));
        System.out.println("5000 >>> 4: " + (Integer.toBinaryString(5000 >>> 4)));
        System.out.println("5000 >>> 8: " + (Integer.toBinaryString(5000 >>> 8)));
        System.out.println("5000 >>> 16: " + (Integer.toBinaryString(5000 >>> 16)));
//        System.out.println("5000 >>> 1" + (Integer.toBinaryString(5000 >>> 32)));
    }


    @Test
    public void printTableA() {
//        System.out.println("5000 >>> 0: " + (tableSizeFor(500)));
        System.out.println("5000 >>> 0: " + (tableSizeFor((0b100000000000000000000001))));
//        System.out.println("5000 >>> 1" + (Integer.toBinaryString(5000 >>> 32)));

        System.out.println("max int: " + Integer.MAX_VALUE);
    }

    private static int tableSizeFor(int cap) {
        int n = cap - 1;
        System.out.println(" >>> 0: " + Integer.toBinaryString(n));
        n |= n >>> 1;
        System.out.println(" >>> 1: " + Integer.toBinaryString(n));
        n |= n >>> 2;
        System.out.println(" >>> 2: " + Integer.toBinaryString(n));
        n |= n >>> 4;
        System.out.println(" >>> 4: " + Integer.toBinaryString(n));
        n |= n >>> 8;
        System.out.println(" >>> 8: " + Integer.toBinaryString(n));
        n |= n >>> 16;
        System.out.println(" >>> 16: " + Integer.toBinaryString(n));
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    @Test
    public void hashMapTest() {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put("key-" + i, i);
        }
    }

    @Test
    public void concurrentHashMapTest() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    }

}