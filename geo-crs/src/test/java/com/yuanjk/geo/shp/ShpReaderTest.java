package com.yuanjk.geo.shp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * 类<code>Doc</code>用于：TODO
 *
 * @author yuanjk
 * @version 1.0
 */
public class ShpReaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void readFile() throws IOException {
        String shpFile = "D:\\data\\西雅图\\Seattle_Analytic_Data\\ZX_WSGC_WSGD\\ZX_WSGC_WSGD.shp";

        ShpReader.readAsGeometry(shpFile);




    }
}