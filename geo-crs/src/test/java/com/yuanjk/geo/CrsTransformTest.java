package com.yuanjk.geo;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;

import static org.junit.Assert.*;

public class CrsTransformTest {

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
    public void findMathTransform() {
    }

    @Test
    public void transformGeometry() {
    }

    @Test
    public void axisOrder() {
    }
}