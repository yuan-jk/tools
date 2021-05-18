package com.yuanjk.geo;

import org.geotools.geometry.jts.JTS;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.wkt.Formattable;
import org.opengis.geometry.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author yuanjk
 * @version 21/3/27
 */
public class CrsTransform {

    public static void main(String[] args) {


    }

    public static void defineCrs() throws FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");


        //Using the “OGC URN” syntax:
        CoordinateReferenceSystem sourceCRSUrn = CRS.decode("urn:ogc:def:ellipsoid:EPSG:6.0:7001");

        //Using the WMS AUTO2 syntax (which requires you pass in your current “position”:
        String lat = "";
        String lon = "";
        CoordinateReferenceSystem sourceCRSWms = CRS.decode("AUTO2:42001," + lat + "," + lon);
    }


    public static void wktCrs() throws FactoryException {
        String wkt = "GEOGCS[" + "\"WGS 84\"," + "  DATUM[" + "    \"WGS_1984\","
                + "    SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],"
                + "    TOWGS84[0,0,0,0,0,0,0]," + "    AUTHORITY[\"EPSG\",\"6326\"]],"
                + "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
                + "  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],"
                + "  AXIS[\"Lat\",NORTH]," + "  AXIS[\"Long\",EAST],"
                + "  AUTHORITY[\"EPSG\",\"4326\"]]";

        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
    }

    public static void crsToWkt(String epsgCode) throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode(epsgCode);
        String wkt = crs.toWKT();
//        System.out.println("wkt for EPSG:32735");
        System.out.println("wkt for "+epsgCode);
        System.out.println(wkt);
    }

    public static void crsToWktFormat() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32735");
        Formattable f = (Formattable) CRS.decode("EPSG:32735", true);
        String wkt = f.toWKT(Citations.ESRI, 2); // use 0 indent for single line

        System.out.println("wkt for EPSG:32735 (ESRI)");
        System.out.println(wkt);
    }


    public static void matchCrs() throws FactoryException {
        String wkt =
                "GEOGCS[\"ED50\",\n" +
                        "  DATUM[\"European Datum 1950\",\n" +
                        "  SPHEROID[\"International 1924\", 6378388.0, 297.0]],\n" +
                        "PRIMEM[\"Greenwich\", 0.0],\n" +
                        "UNIT[\"degree\", 0.017453292519943295]]";
        CoordinateReferenceSystem example = CRS.parseWKT(wkt);

        String code = CRS.lookupIdentifier(example, true); // should be "EPSG:4230"
        CoordinateReferenceSystem crs = CRS.decode(code);
    }

    public static void findMathTransform() throws FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);


        String wkt = "PROJCS[\"NAD83 / BC Albers\"," +
                "GEOGCS[\"NAD83\", " +
                "  DATUM[\"North_American_Datum_1983\", " +
                "    SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], " +
                "    TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "    AUTHORITY[\"EPSG\",\"6269\"]], " +
                "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], " +
                "  UNIT[\"degree\", 0.017453292519943295], " +
                "  AXIS[\"Lon\", EAST], " +
                "  AXIS[\"Lat\", NORTH], " +
                "  AUTHORITY[\"EPSG\",\"4269\"]], " +
                "PROJECTION[\"Albers_Conic_Equal_Area\"], " +
                "PARAMETER[\"central_meridian\", -126.0], " +
                "PARAMETER[\"latitude_of_origin\", 45.0], " +
                "PARAMETER[\"standard_parallel_1\", 50.0], " +
                "PARAMETER[\"false_easting\", 1000000.0], " +
                "PARAMETER[\"false_northing\", 0.0], " +
                "PARAMETER[\"standard_parallel_2\", 58.5], " +
                "UNIT[\"m\", 1.0], " +
                "AXIS[\"x\", EAST], " +
                "AXIS[\"y\", NORTH], " +
                "AUTHORITY[\"EPSG\",\"3005\"]]";
        CoordinateReferenceSystem example = CRS.parseWKT(wkt);
        targetCRS = CRS.decode("EPSG:4326");
        //When using a CoordinateReferenceSystem that has been parsed from WKT you will often need to “relax” the accuracy
        // by setting the lenient parameter to true when searching with findMathTransform
        transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
    }

    public static void transformGeometry() throws FactoryException {
//        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
//        Geometry targetGeometry = JTS.transform( sourceGeometry, transform);
//        //Transforming an ISO Geometry is more straight forward:
//        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
//        Geometry target = geometry.transform( targetCRS );
    }

    public static void axisOrder(CoordinateReferenceSystem coordinateReferenceSystem) {
//        if (CRS.getAxisOrder(coordinateReferenceSystem) == CRS.AxisOrder.LAT_LON) {
        if (CRS.getAxisOrder(coordinateReferenceSystem) == CRS.AxisOrder.NORTH_EAST) {
            // lat lon
        }

        CoordinateReferenceSystem crs = CRS.getHorizontalCRS(DefaultEngineeringCRS.GENERIC_2D);
        if (CRS.getAxisOrder(crs) == CRS.AxisOrder.INAPPLICABLE) {
            // someone just made this up
        }
    }

}
