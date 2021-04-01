package com.yuanjk.geo;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;


/**
 * @author yuanjk
 * @version 21/3/29
 */
public class GeometryRelation {


    public static Geometry readGeometryFromWkt(String wkt) throws ParseException {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        WKTReader reader = new WKTReader(geometryFactory);
//        Geometry geometry = reader.read("MultiPolygon (((106.70206058681142736 29.57404409748362184, " +
//                "106.70640989759029083 29.57283633583158533, 106.70015424526575032 29.57061580126033462, " +
//                "106.70015424526575032 29.57061580126033462, 106.70206058681142736 29.57404409748362184)))");

        Geometry geometry = reader.read(wkt);


        System.out.println(geometry.getGeometryType());
        return geometry;
    }


}
