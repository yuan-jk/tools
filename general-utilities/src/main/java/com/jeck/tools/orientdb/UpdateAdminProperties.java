package com.jeck.tools.orientdb;

import com.alibaba.fastjson.JSON;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureauImpl.OrientDBCimDataSpaceImpl;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OGlobalProperty;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateAdminProperties {

    private static String dataBase = "pcopcim";
    private static String userName = "root";
    private static String password = "wyc";


    public static void addAdminProperty(String tableName, String adminGeo) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(OGlobalConfiguration.NETWORK_HTTP_SESSION_EXPIRE_TIMEOUT.getKey(), 60 * 1000);
        configMap.put(OGlobalConfiguration.NETWORK_TOKEN_EXPIRE_TIMEOUT.getKey(), 600 * 1000);
        configMap.put(OGlobalConfiguration.NETWORK_SOCKET_TIMEOUT.getKey(), 600 * 1000);
        configMap.put(OGlobalConfiguration.NETWORK_REQUEST_TIMEOUT.getKey(), 600 * 1000);
        configMap.put(OGlobalConfiguration.COMMAND_TIMEOUT.getKey(), 600 * 1000);
//        configMap.put(OGlobalProperty, 600 * 1000);
        OrientDBConfig config = OrientDBConfig.builder().fromMap(configMap).build();
//        OrientDB orientDB = new OrientDB("remote:127.0.0.1/", OrientDBConfig.defaultConfig());
        OrientDB orientDB = new OrientDB("remote:localhost/", config);
        ODatabasePool pool = new ODatabasePool(orientDB, dataBase, userName, password);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT FROM ( SELECT *, ST_Contains(ST_GeomFromText(\"");
        sqlBuilder.append(adminGeo);
        sqlBuilder.append("\"),ST_GeomFromText(CIM_GeographicInformation)) AS flag FROM ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" ) WHERE flag = true limit 10");
//        String statement = "SELECT FROM ( SELECT *, ST_Contains(ST_GeomFromText(adminGeo),ST_GeomFromText(CIM_GeographicInformation)) AS flag FROM tableName ) WHERE flag = true";
        String statement = sqlBuilder.toString();
//        String statement = "select from GLD_IH_FACT_china_railways_0225 limit 10";
        System.out.println("query statement: [" + statement + "]");
//        Map<String, Object> params = new HashMap<>();
//        params.put("tableName", tableName);
//        params.put("adminGeo", adminGeo);

//        try (ODatabaseSession db = orientDB.open(dataBase, userName, password)) {
        try (ODatabaseSession db = pool.acquire()) {
            OResultSet rs = db.query(statement);
            while (rs.hasNext()) {
                OResult row = rs.next();
                System.out.println("RID: " + row.getIdentity());
            }
        }
    }

    public static void addAdminProperty(String tableName, String adminGeo, String adminName, OrientGraph orientGraph) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT FROM ( SELECT *, ST_Contains(ST_GeomFromText(\"");
        sqlBuilder.append(adminGeo);
        sqlBuilder.append("\"),ST_GeomFromText(CIM_GeographicInformation)) AS flag FROM ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" ) WHERE flag = true");
        String statement = sqlBuilder.toString();
        System.out.println("query statement: [" + statement + "]");

        Iterable<Vertex> vertices = orientGraph.command(new OCommandSQL(statement)).execute();
        Iterator<Vertex> vertexIterator = vertices.iterator();
        int count = 0;
        while (vertexIterator.hasNext()) {
            Vertex v = vertexIterator.next();
            v.setProperty("adminName", adminName);
//            System.out.println("RID: " + v.getId());
            count++;
        }
        System.out.println("admin name [" + adminName + "], count[" + count + "]");
    }

    public static void main(String[] args) throws IOException {
        String fileName = "D:\\data\\spatial_data\\China\\admin\\BeiJingCityQu.txt";
        String tableName = "GLD_IH_FACT_china_railways_0225";
        String adminName = "东城区";
        String adminGeo = "MultiPolygon (((116.403 39.972,116.402 39.97,116.405 39.97,116.405 39.964,116.406 39.962,116.408 39.963,116.408 39.961,116.419 39.961,116.419 39.958,116.424 39.958,116.424 39.955,116.423 39.956,116.423 39.953,116.425 39.951,116.423 39.95,116.424 39.949,116.43 39.948,116.429 39.951,116.433 39.951,116.433 39.948,116.435 39.947,116.434 39.945,116.432 39.945,116.436 39.944,116.439 39.945,116.441 39.944,116.438 39.94,116.438 39.927,116.428 39.927,116.43 39.901,116.442 39.902,116.443 39.9,116.44 39.899,116.441 39.899,116.441 39.893,116.445 39.892,116.445 39.891,116.446 39.891,116.445 39.891,116.445 39.889,116.44 39.89,116.44 39.889,116.438 39.889,116.439 39.878,116.437 39.871,116.415 39.871,116.41 39.87,116.411 39.87,116.408 39.869,116.409 39.868,116.409 39.866,116.41 39.865,116.41 39.865,116.41 39.862,116.408 39.862,116.407 39.859,116.404 39.858,116.394 39.858,116.394 39.857,116.389 39.857,116.389 39.861,116.387 39.861,116.385 39.864,116.381 39.864,116.381 39.866,116.371 39.865,116.375 39.867,116.375 39.87,116.393 39.871,116.392 39.897,116.39 39.898,116.389 39.907,116.386 39.907,116.385 39.91,116.387 39.912,116.386 39.921,116.393 39.922,116.393 39.927,116.391 39.927,116.39 39.939,116.388 39.939,116.387 39.956,116.38 39.955,116.381 39.96,116.383 39.96,116.383 39.962,116.385 39.962,116.391 39.962,116.392 39.961,116.393 39.961,116.394 39.961,116.395 39.961,116.395 39.961,116.397 39.961,116.397 39.96,116.402 39.961,116.401 39.968,116.401 39.973,116.403 39.973,116.403 39.972)))";
        CimDataSpace cds = null;
        try {
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace("pcopcim");
            OrientGraph graph = ((OrientDBCimDataSpaceImpl) cds).getGraph();
            Map<String, String> adminMap = readAdminInfo(fileName);
            for (Map.Entry<String, String> entry : adminMap.entrySet()) {
                addAdminProperty(tableName, entry.getValue(), entry.getKey(), graph);
            }
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }


    }


    public static Map<String, String> readAdminInfo(String fileName) throws IOException {
        Map<String, String> adminInfo = new HashMap<>();
        BufferedReader br = Files.newBufferedReader(Paths.get(fileName));

        String line = br.readLine();
        while (line != null) {
            String[] arr = line.split(" ", 2);
            if (arr.length == 2) {
                adminInfo.put(arr[0], arr[1]);
            }
            line = br.readLine();
        }
        return adminInfo;
    }

}
