package com.jeck.tools.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class ChineseAnalyzer {

    public void print(Analyzer analyzer) throws Exception {
//        String text = "Lucene自带多种分词器，其中对中文分词支持比较好的是smartcn。";
        String text = "广联达河南省海淀区清河西三旗转盘西侧8号平房东侧第7间有限公司黔东南州千百度装饰有限公司山东省日照市东港区泰安路与北京路交汇处西100米沿街102号U320276395";
        TokenStream tokenStream = analyzer.tokenStream("content", text);
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(new String(attribute.toString()));
        }
    }


}
