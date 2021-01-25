package com.jeck.tools.postgis;

import org.postgis.PGbox3d;
import org.postgis.PGgeometry;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class JavaGIS {
    public static void main(String[] args) {
        java.sql.Connection conn;
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
            ResultSet r = s.executeQuery("select geom,gid from building_pl");
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
}