package com.yuanjk.geo.shp;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * 类<code>Doc</code>用于：TODO
 *
 * @author yuanjk
 * @version 1.0
 */
public class ShpReader {

    /**
     * @param filePath
     * @throws IOException
     */
    public static List<Geometry> readAsGeometry(String filePath) throws IOException {
        List<Geometry> geometries = new ArrayList<>();

        ShapefileDataStore dataStore = new ShapefileDataStore(Paths.get(filePath).toUri().toURL());
//        dataStore.getNames().forEach(System.out::println);

        SimpleFeatureSource simpleFeatureSource = dataStore.getFeatureSource(dataStore.getNames().get(0));

        SimpleFeatureCollection featureCollection = simpleFeatureSource.getFeatures();
        try (SimpleFeatureIterator featureIterator = featureCollection.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature simpleFeature = featureIterator.next();
                Geometry geometry = ((Geometry) simpleFeature.getDefaultGeometry());
                if (geometry == null) {
                    System.out.println("no geometry info is ignore");
                    continue;
                }
                Collection<Property> properties = simpleFeature.getProperties();
                Map<String, Object> userData = new HashMap<>();
                properties.forEach(p -> userData.put(p.getName().getLocalPart(), p.getValue()));
                geometry.setUserData(userData);
                geometries.add(geometry);
            }
        }
        return geometries;
    }


}
