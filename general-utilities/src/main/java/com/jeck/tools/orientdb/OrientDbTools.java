package com.jeck.tools.orientdb;

import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;

import java.io.*;
import java.util.Collection;

/**
 * @author yuanjk
 * @version 21/9/17
 */
public class OrientDbTools {
    private static ODatabasePool databasePool;

    public static void openDatabasePool(String url, String dbName, String usr, String pwd) {
        OrientDB orientDB = new OrientDB(url, OrientDBConfig.defaultConfig());
        databasePool = new ODatabasePool(orientDB, dbName, usr, pwd);
    }

    public static void closePool() {
        if (databasePool != null) {
            databasePool.close();
        }
    }

    public static ODatabaseDocument getDatabase() {
        return databasePool != null ? databasePool.acquire() : null;
    }

    public static void listAllCls(ODatabaseDocument database, String filePath) {
//        database.dropCluster()
        OSchema schema = database.getMetadata().getSchema();

        System.out.println("total classes: " + schema.countClasses());

        Collection<OClass> clss = schema.getClasses();

        try (FileWriter fw = new FileWriter(filePath)) {
            for (OClass oClass : clss) {
                oClass.count(false);
//                fw.write(oClass.getName() + "=" + oClass.count() + "\\n");
                String st = oClass.getName() + "=" + oClass.count(false) + "\n";
                fw.write(st);
                System.out.println(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCls(ODatabaseDocument database, String clsName) {
//        database.dropCluster()
        long startDate = System.currentTimeMillis();
        OSchema schema = database.getMetadata().getSchema();

        if (schema.existsClass(clsName)) {
            OClass cls = schema.getClass(clsName);
            int[] clIds = cls.getClusterIds();
            for (int clId : clIds) {
                database.dropCluster(clId, true);
            }
            schema.dropClass(clsName);
        }

        System.out.println("used million seconds: " + (System.currentTimeMillis() - startDate));
    }

}
