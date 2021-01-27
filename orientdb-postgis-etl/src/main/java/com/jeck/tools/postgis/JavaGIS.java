package com.jeck.tools.postgis;

import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.apache.commons.lang.ArrayUtils;
import org.postgis.PGbox3d;
import org.postgis.PGgeometry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class JavaGIS {

    private static final String GEO_INFO_PROPERTY = "CIM_GeographicInformation";

    public static void main(String[] args) {
        System.out.println("input parameters: [" + ArrayUtils.toString(args) + "]");
        String tableName = args[1];
        String objectTypeId = args[2];
        String spaceName = args[3];
        boolean isUpdate = Boolean.valueOf(args[4]);

        OrientDB orientDB = new OrientDB(args[0], OrientDBConfig.defaultConfig());
//        OrientDB orientDB = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabasePool pool = new ODatabasePool(orientDB, spaceName, "root", "wyc");
//        ODatabaseDocument db = orientDB.open("demodb", "root", "wyc");
        try {
            updateGeometry(pool, tableName, objectTypeId, isUpdate);
        } finally {
            pool.close();
//            db.close();
            orientDB.close();
        }
    }

    public static void updateGeometry(ODatabasePool oDatabasePool, String tableName, String objectTypeId,
                                      boolean isUpdate) {
        Connection conn;
        try {
            /*
             * Load the JDBC driver and establish a connection.
             */
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://101.200.45.96:5432/gyd_database";
            conn = DriverManager.getConnection(url, "postgres", "!QAZ@WSX");
            /*
             * Add the geometry types to the connection. Note that you
             * must cast the connection to the pgsql-specific connection
             * implementation before calling the addDataType() method.
             */
//            ((org.postgresql.PGConnection) conn).addDataType("geometry", "org.postgis.PGgeometry");
//            ((org.postgresql.PGConnection) conn).addDataType("box3d", "org.postgis.PGbox3d");
            ((org.postgresql.PGConnection) conn).addDataType("geometry", PGgeometry.class);
            ((org.postgresql.PGConnection) conn).addDataType("box3d", PGbox3d.class);
            /*
             * Create a statement and execute a select query.
             */
            Statement s = conn.createStatement();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select gid, ST_AsText(ST_Force2D(geom), 3) as wktGeo from ");
            sqlBuilder.append(tableName);
            System.out.println("post gis sql: [" + sqlBuilder.toString() + "]");
            ResultSet r = s.executeQuery(sqlBuilder.toString());
            while (r.next()) {
                String wktGeometry = r.getString(2);
                int gid = r.getInt(1);
                System.out.println("Row gid: [" + gid + "]");
                System.out.println("geo info [" + wktGeometry + "]");
                try (ODatabaseDocument db = oDatabasePool.acquire()) {
                    updateOrientRecordByProperty(db, objectTypeId, "gid", String.valueOf(gid), wktGeometry, isUpdate);
                }
            }
            s.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void officialExample() {
        java.sql.Connection conn;
        try {
            /*
             * Load the JDBC driver and establish a connection.
             */
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/database";
            conn = DriverManager.getConnection(url, "postgres", "");
            /*
             * Add the geometry types to the connection. Note that you
             * must cast the connection to the pgsql-specific connection
             * implementation before calling the addDataType() method.
             */
            ((org.postgresql.PGConnection) conn).addDataType("geometry", org.postgis.PGgeometry.class);
            ((org.postgresql.PGConnection) conn).addDataType("box3d", org.postgis.PGbox3d.class);
            /*
             * Create a statement and execute a select query.
             */
            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery("select geom,id from geomtable");
            while (r.next()) {
                /*
                 * Retrieve the geometry as an object then cast it to the geometry type.
                 * Print things out.
                 */
                PGgeometry geom = (PGgeometry) r.getObject(1);
                int id = r.getInt(2);
                System.out.println("Row " + id + ":");
                System.out.println(geom.toString());
            }
            s.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateOrientRecordByProperty(ODatabaseDocument db, String objectTypeId, String key, String value,
                                                    Object uValue, boolean isUpdate) {
        Map<String, Object> params = new HashMap<>();
        params.put("objectTypeId", objectTypeId);
        params.put("key", key);
        params.put("value", value);
        String stm = "SELECT FROM :objectTypeId WHERE gid = :value";
        System.out.println("orient sql: [" + stm + "]");

        OResultSet rs = db.query(stm, params);

        while (rs.hasNext()) {
            if (isUpdate) {
                rs.next().getVertex().ifPresent(x -> {
                    x.setProperty(GEO_INFO_PROPERTY, uValue);
                    x.save();
                    System.out.println("one record is updated [" + x.getIdentity() + "]");
                });
            } else {
                rs.next().getVertex().ifPresent(x -> {
                    System.out.println("one record will is updated [" + x.getIdentity() + "]");
                });
            }
        }
        rs.close();
    }

}