package com.yuanjk.geo.triangulate;

import com.yuanjk.geo.shp.ShpReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类<code>Doc</code>用于：TODO
 *
 * @author yuanjk
 * @version 1.0
 */
public class ThiessenPolygonsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void randomPoints() throws URISyntaxException, IOException {

        VoronoiDiagramBuilder diagramBuilder = new VoronoiDiagramBuilder();
        String polygonFile =
                Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("shp/clip" +
                        "/RandomPoints.shp")).toURI()).toFile().getAbsolutePath();
        List<Geometry> geometries = ShpReader.readAsGeometry(polygonFile);

        List<Coordinate> coordinateList = new ArrayList<>();


        geometries.forEach(g -> coordinateList.add(g.getCoordinate()));

        diagramBuilder.setSites(coordinateList);
        diagramBuilder.setClipEnvelope(new Envelope(447100, 448360, 4419900, 4421050));

        Geometry results = diagramBuilder.getDiagram(new GeometryFactory());

        WKTWriter writer = new WKTWriter();
        System.out.println(writer.write(results));


        Geometry triangles = diagramBuilder.getSubdivision().getTriangles(new GeometryFactory());
        System.out.println("triangles: " + writer.write(triangles));
    }

    @Test
    public void thiessenPolygons() throws URISyntaxException, IOException {
        VoronoiDiagramBuilder diagramBuilder = new VoronoiDiagramBuilder();
        String polygonFile =
                Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("shp/clip" +
                        "/RandomPoints.shp")).toURI()).toFile().getAbsolutePath();
        List<Geometry> geometries = ShpReader.readAsGeometry(polygonFile);

        List<Coordinate> coordinateList = new ArrayList<>();

        geometries.forEach(g -> coordinateList.add(g.getCoordinate()));

        //添加特殊数据
        Coordinate crd = coordinateList.get(0);
        coordinateList.add(new Coordinate(crd.x, crd.y + 100));
        coordinateList.add(new Coordinate(crd.x, crd.y + 100));
        coordinateList.add(new Coordinate(crd.x, crd.y + 200));

        coordinateList.add(new Coordinate(crd.x + 100, crd.y));
        coordinateList.add(new Coordinate(crd.x + 100, crd.y));
        coordinateList.add(new Coordinate(crd.x + 200, crd.y));

//        coordinateList.add(new Coordinate(crd.x + 10, crd.y));

        diagramBuilder.setSites(coordinateList);
        diagramBuilder.setClipEnvelope(new Envelope(447100, 448660, 4419900, 4421350));
        diagramBuilder.setTolerance(20d);

        Geometry results = diagramBuilder.getDiagram(new GeometryFactory());

        WKTWriter writer = new WKTWriter();
        System.out.println("thiessen polygon: \n" + writer.write(results));

        Geometry triangles = diagramBuilder.getSubdivision().getTriangles(new GeometryFactory());
        System.out.println("triangles: \n" + writer.write(triangles));

        GeometryFactory geometryFactory = new GeometryFactory();
        Point[] pointArray = coordinateList.stream().map(geometryFactory::createPoint).toArray(Point[]::new);
        Geometry multiPoints = geometryFactory.createMultiPoint(pointArray);
        System.out.println("new input: \n" + writer.write(multiPoints));

        for (Point point : pointArray) {
            System.out.println(writer.write(point));
        }
    }


}