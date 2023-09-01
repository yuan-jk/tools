package cn.com.yuanjk.jts;

import com.yuanjk.geo.shp.GeoJSONFile;
import com.yuanjk.geo.shp.ShpReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

class DistributedDifferenceTest {

    @BeforeEach
    void setUp() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void distributedDiff() throws IOException {

//        String baseJsonFile = "D:\\tmp\\崇明岛Boundary.json", shpFile = "D:\\tmp\\崇明岛\\崇明岛buffer-20.shp";
        String baseJsonFile = "D:\\tmp\\上海主城区边界.json", shpFile = "D:\\tmp\\上海主城区\\上海主城区buffer-20.shp";
        long startDate = System.currentTimeMillis();
        List<Geometry> baseGeometries = GeoJSONFile.readGeometry(baseJsonFile, StandardCharsets.UTF_8);
        List<Geometry> eraseGeometries = ShpReader.readAsGeometry(shpFile);
        Polygon basePolygon = ((Polygon) baseGeometries.get(0).getGeometryN(0));
        WKTWriter wktWriter = new WKTWriter();

        System.out.println("is valid geometry: " + basePolygon.isValid());
//        System.out.println("is valid geometry: " + basePolygon.buffer(0).isValid());
//        System.out.println("is valid geometry: " + wktWriter.write(basePolygon.buffer(0)));
        System.out.println("input erase geometry size: " + eraseGeometries.size());
        DistributedDifference counter = new DistributedDifference(((Polygon) basePolygon.buffer(0)),
                eraseGeometries.stream().map(f -> (Polygon) f.getGeometryN(0)).collect(Collectors.toList()));
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(counter);
        List<Polygon> resultPolygons = counter.join();
        System.out.println("used million seconds: " + (System.currentTimeMillis() - startDate));
//        GeometryCollection geometryCollection = new GeometryCollection(resultPolygons.toArray(new Geometry[0]),
//                new GeometryFactory());
//        System.out.println(wktWriter.write(geometryCollection));
        Path jsonFilePath = Paths.get(baseJsonFile);
        Path resultPath = jsonFilePath.resolveSibling("difference-result2.geojson");

        Files.write(resultPath, resultPolygons.stream().map(wktWriter::write).collect(Collectors.toList()));
    }

}