package com.jeck.tools.orientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanjk
 * @version 22/3/28
 */
public class SaveInstancesToFile {

    public static void saveToFile(ODatabaseDocument database, String tableName, String filePath) throws IOException {

        OResultSet resultSet = database.query("SELECT * FROM ?", tableName);

//        System.out.println("result size: " + resultSet.stream().count());
        try (FileWriter fw = new FileWriter(filePath+".tsv")) {
            List<String> propertyNames = new ArrayList<>();
            while (resultSet.hasNext()) {
                OResult oResult = resultSet.next();
                if (propertyNames.size() == 0) {
                    propertyNames = new ArrayList<>(oResult.getPropertyNames());
                }
                StringBuilder sb = new StringBuilder();
                propertyNames.forEach(e -> {
                    Object pp = oResult.getProperty(e);
                    if (pp == null) {
                        sb.append("").append("\t");
                    } else {
                        sb.append(pp.toString()).append("\t");
                    }
                });
                System.out.println(sb.toString());
                fw.write(sb.substring(0, sb.lastIndexOf("\t")));
                fw.write("\n");
            }
        }

    }


}
