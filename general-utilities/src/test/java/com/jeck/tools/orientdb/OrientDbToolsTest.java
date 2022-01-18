package com.jeck.tools.orientdb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author yuanjk
 * @version 21/9/17
 */
public class OrientDbToolsTest {

    @Before
    public void setUp() throws Exception {
//        OrientDbTools.openDatabasePool("remote:10.0.104.230", "pcopcim", "root", "gldokok");
        OrientDbTools.openDatabasePool("remote:10.2.112.47", "pcopcim-20210922020002", "root", "td");
    }

    @After
    public void tearDown() throws Exception {
        OrientDbTools.closePool();
    }

    @Test
    public void getDatabase() {
    }

    @Test
    public void listAllCls() {
        String filePath = "G:\\tmp\\fuzhou-230-all-classes-0923.txt";
        OrientDbTools.listAllCls(OrientDbTools.getDatabase(), filePath);

    }


    @Test
    public void deleteCls() {
//        OrientDbTools.deleteCls(OrientDbTools.getDatabase(), "GLD_DIMENSION_test_zhiliangguanli_a1595963423000");
        OrientDbTools.deleteCls(OrientDbTools.getDatabase(), "GLD_DIMENSION_test_zhiliangguanli_a1592766620000");
    }

}