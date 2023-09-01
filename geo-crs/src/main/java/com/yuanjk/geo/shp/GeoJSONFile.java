package com.yuanjk.geo.shp;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeoJSONFile {

    public static List<Geometry> readGeometry(String jsonFile, Charset charset) throws IOException {
        Path path = Paths.get(jsonFile);
        File file = path.toFile();
        List<Geometry> geometries = new ArrayList<>();
        if (file.exists()) {
            List<String> lines = Files.readAllLines(path, charset);
            if (lines.size() > 0) {
                WKTReader wktReader = new WKTReader();
                for (String line : lines) {
                    try {
                        geometries.add(wktReader.read(line));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return geometries;
    }

}
