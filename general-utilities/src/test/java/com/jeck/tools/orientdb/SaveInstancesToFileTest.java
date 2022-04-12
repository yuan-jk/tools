package com.jeck.tools.orientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author yuanjk
 * @version 22/3/28
 */
public class SaveInstancesToFileTest {

    @Before
    public void setUp() throws Exception {
//        OrientDbTools.openDatabasePool("remote:10.2.112.47", "pcopcim-20210922020002", "root", "td");
//        OrientDbTools.openDatabasePool("remote:39.106.23.94:11452", "gyd", "root", "wyc");
        OrientDbTools.openDatabasePool("remote:192.168.200.124", "gyd", "root", "wyc");
    }

    @After
    public void tearDown() throws Exception {
        OrientDbTools.closePool();
    }

    @Test
    public void saveToFileTest() throws IOException {
        ODatabaseDocument database = OrientDbTools.getDatabase();
//        String tableName = "GLD_IH_FACT_SZZGGJ";
//        String filePath = "GLD_IH_FACT_SZZGGJ";

//        String tableName = "GLD_IH_FACT_PhysicEquipment";
//        String filePath = "GLD_IH_FACT_PhysicEquipment";

//        String tableName = "GLD_IH_FACT_PhysicFacility";
//        String filePath = "GLD_IH_FACT_PhysicFacility";

//        String tableName = "GLD_IH_FACT_SmallPlaque";
//        String filePath = "GLD_IH_FACT_SmallPlaque";

//        String tableName = "GLD_IH_FACT_FunctionArea";
//        String filePath = "GLD_IH_FACT_FunctionArea";

//        String tableName = "GLD_IH_FACT_viewpoint";
//        String filePath = "GLD_IH_FACT_viewpoint";

        String tableName = "GLD_IH_FACT_region_point";
        String filePath = "GLD_IH_FACT_region_point";

        SaveInstancesToFile.saveToFile(database, tableName, filePath);
    }
}