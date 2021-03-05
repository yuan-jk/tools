package com.jeck.tools.query;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.jeck.tools.query.ExpressionUtil.PropertyTypeEnum;
import com.jeck.tools.query.ExpressionUtil.OperatorEnum;
import com.jeck.tools.query.ExpressionUtil.LogicalOperatorEnum;

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
        ExpressionBean eb1 = new ExpressionBean("company", "企业招标行为", "总次数", PropertyTypeEnum.INT,
                OperatorEnum.GreaterThanFilteringItem, "123");
        ExpressionBean eb2 = new ExpressionBean("company", "营业收入", "总金额", PropertyTypeEnum.DOUBLE,
                OperatorEnum.GreaterThanEqualFilteringItem, "9999.9");

        ExpressionBean[] ea12 = {eb1, eb2};
        ExpressionBean eb12 = new ExpressionBean(LogicalOperatorEnum.AND, Arrays.asList(ea12));

        ExpressionBean eb3 = new ExpressionBean("company", "企业注册信息", "注册地址", PropertyTypeEnum.STRING,
                OperatorEnum.SimilarFilteringItem, "北京");
        ExpressionBean eb4 = new ExpressionBean("company", "企业注册信息", "办公地址", PropertyTypeEnum.STRING,
                OperatorEnum.SimilarFilteringItem, "北京");

        ExpressionBean[] ea34 = {eb3, eb4};
        ExpressionBean eb34 = new ExpressionBean(LogicalOperatorEnum.OR, Arrays.asList(ea34));

        ExpressionBean eb5 = new ExpressionBean("company", "企业注册信息", "创建时间", PropertyTypeEnum.TIMESTAMP,
                OperatorEnum.BetweenFilteringItem, "946656000000,1577808000000");

        ExpressionBean[] ea345 = {eb34, eb5};

        ExpressionBean eb345 = new ExpressionBean(LogicalOperatorEnum.AND, Arrays.asList(ea345));

        ExpressionBean[] ea12345 = {eb12, eb345};

        ExpressionBean eb = new ExpressionBean(LogicalOperatorEnum.AND, Arrays.asList(ea12345));

        String js = JSON.toJSONString(eb);
        System.out.println(js);
        return js;
    }

    public static String deserializeObject() {
        String input = "{\"logicalOperator\":\"AND\",\"subExpression\":[{\"logicalOperator\":\"AND\",\"subExpression\":[{\"dataSet\":\"企业招标行为\",\"objectTypeId\":\"company\",\"operator\":\"GreaterThanFilteringItem\",\"property\":\"总次数\",\"value\":\"123\",\"valueType\":\"INT\"},{\"dataSet\":\"营业收入\",\"objectTypeId\":\"company\",\"operator\":\"GreaterThanEqualFilteringItem\",\"property\":\"总金额\",\"value\":\"9999.9\",\"valueType\":\"DOUBLE\"}]},{\"logicalOperator\":\"AND\",\"subExpression\":[{\"logicalOperator\":\"OR\",\"subExpression\":[{\"dataSet\":\"企业注册信息\",\"objectTypeId\":\"company\",\"operator\":\"SimilarFilteringItem\",\"property\":\"注册地址\",\"value\":\"北京\",\"valueType\":\"STRING\"},{\"dataSet\":\"企业注册信息\",\"objectTypeId\":\"company\",\"operator\":\"SimilarFilteringItem\",\"property\":\"办公地址\",\"value\":\"北京\",\"valueType\":\"STRING\"}]},{\"dataSet\":\"企业注册信息\",\"objectTypeId\":\"company\",\"operator\":\"BetweenFilteringItem\",\"property\":\"创建时间\",\"value\":\"946656000000,1577808000000\",\"valueType\":\"TIMESTAMP\"}]}]}\n";

        ExpressionBean eb = JSON.parseObject(input, ExpressionBean.class);
        System.out.println(expressionParser(eb));
        return null;
    }

    public static String expressionParser(ExpressionBean eb) {
        StringBuilder sb = new StringBuilder();
        List<ExpressionBean> subExpList = eb.getSubExpression();
        if (CollectionUtils.isNotEmpty(subExpList)) {
            String operator = eb.getLogicalOperator().toString();
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
        } else if (StringUtils.isNotBlank(eb.getProperty())) {
            FilterInterface baseFilter = FilterFactory
                    .createFilter(eb.getDataSet(), eb.getProperty(), eb.getOperator(), eb.getValueType(),
                            eb.getValue());
            sb.append(baseFilter.getCondition());
        } else {
            System.out.println("error ");
        }
        System.out.println("sub condition: [" + sb.toString() + "]");
        return sb.toString();
    }


}
