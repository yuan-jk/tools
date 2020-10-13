package com.jeck.tools.lucene;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChineseAnalyzerTest {

    private static ChineseAnalyzer chineseAnalyzer = new ChineseAnalyzer();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void standAnalyzer() throws Exception {
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        chineseAnalyzer.print(standardAnalyzer);
    }


    @Test
    public void smartAnalyzer() throws Exception {
        SmartChineseAnalyzer smartChineseAnalyzer = new SmartChineseAnalyzer();
        chineseAnalyzer.print(smartChineseAnalyzer);
    }

    @Test
    public void cJKAnalyzer() throws Exception {
        CJKAnalyzer cJKAnalyzer = new CJKAnalyzer();
        chineseAnalyzer.print(cJKAnalyzer);
    }


}