package com.jeck.tools.query;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yuanjk
 * @version 21/3/4
 */
public class QueryExpression {

    public static void main(String[] args) {

//        serializeObject();
        deserializeObject();
    }


    public static String serializeObject() {
        ExpressionBean eb1 = new ExpressionBean(null, "property", "企业招标行为", "总次数", "int", ">=", "123");
        ExpressionBean eb2 = new ExpressionBean(null, "property", "企业招标行为", "总金额", "double", "<", "9999.9");

        ExpressionBean[] ea12 = {eb1, eb2};
        ExpressionBean eb12 = new ExpressionBean("and", Arrays.asList(ea12));

        ExpressionBean eb3 = new ExpressionBean(null, "property", "企业注册信息", "industry", "string", "=", "9999.9");
        ExpressionBean eb4 = new ExpressionBean(null, "property", "企业注册信息", "农业", "string", "like", "122");

        ExpressionBean[] ea34 = {eb3, eb4};
        ExpressionBean eb34 = new ExpressionBean("and", Arrays.asList(ea34));

        ExpressionBean eb5 = new ExpressionBean(null, "property", "企业注册信息", "beijing", "long", "in", "666");

        ExpressionBean[] ea345 = {eb34, eb5};

        ExpressionBean eb345 = new ExpressionBean("and", Arrays.asList(ea345));

        ExpressionBean[] ea12345 = {eb12, eb345};

        ExpressionBean eb = new ExpressionBean("and", Arrays.asList(ea12345));

        String js = JSON.toJSONString(eb);
        System.out.println(js);
        return js;
    }

    public static String deserializeObject() {
        String input = "{\"operator\":\"and\",\"subExpression\":[{\"operator\":\"and\",\"subExpression\":[{\"category\":\"企业招标行为\",\"compare\":\">=\",\"field\":\"总次数\",\"type\":\"property\",\"value\":\"123\",\"value_type\":\"int\"},{\"category\":\"企业招标行为\",\"compare\":\"<\",\"field\":\"总金额\",\"type\":\"property\",\"value\":\"9999.9\",\"value_type\":\"double\"}]},{\"operator\":\"and\",\"subExpression\":[{\"operator\":\"and\",\"subExpression\":[{\"category\":\"企业注册信息\",\"compare\":\"=\",\"field\":\"industry\",\"type\":\"property\",\"value\":\"9999.9\",\"value_type\":\"string\"},{\"category\":\"企业注册信息\",\"compare\":\"like\",\"field\":\"农业\",\"type\":\"property\",\"value\":\"122\",\"value_type\":\"string\"}]},{\"category\":\"企业注册信息\",\"compare\":\"in\",\"field\":\"beijing\",\"type\":\"property\",\"value\":\"666\",\"value_type\":\"long\"}]}]}\n";

        ExpressionBean eb = JSON.parseObject(input, ExpressionBean.class);
        System.out.println(expressionParser(eb));
        return null;
    }

    public static String expressionParser(ExpressionBean eb) {
        StringBuilder sb = new StringBuilder();
        List<ExpressionBean> subExpList = eb.getSubExpression();
        if (CollectionUtils.isNotEmpty(subExpList)) {
            String operator = eb.getOperator();
            List<String> subCondList = new ArrayList<>();
            subExpList.forEach(ep -> subCondList.add(expressionParser(ep)));

            if (CollectionUtils.isNotEmpty(subCondList)) {
                if (subCondList.size() > 1) {
                    boolean firstRound = true;
                    for (String subCond : subCondList) {
                        if (firstRound) {
                            firstRound = false;
                            sb.append(" ( ");
                            sb.append(subCond);
                        } else {
                            sb.append(" ").append(operator);
                            sb.append(" ").append(subCond);
                        }
                    }
                    sb.append(" ) ");
                } else {
                    sb.append(" ").append(subCondList);
                }
            } else {
                System.out.println("sub condition is empty");
            }
        } else if (StringUtils.isNotBlank(eb.getField())) {
            sb.append(" ").append(eb.getField());
            sb.append(" ").append(eb.getCompare());
            sb.append(" '").append(eb.getValue()).append("'");
        } else {
            System.out.println("error ");
        }
        System.out.println("sub condition: [" + sb.toString() + "]");
        return sb.toString();
    }


}
