package com.jeck.tools.query;

import java.util.List;

/**
 * @author yuanjk
 * @version 21/3/4
 */
public class ExpressionBean {

    private String operator;
    private List<ExpressionBean> subExpression;
    private String type;
    private String category;
    private String field;
    private String value_type;
    private String compare;
    private String value;

    public ExpressionBean(String operator, List<ExpressionBean> subExpression) {
        this.operator = operator;
        this.subExpression = subExpression;
    }

    public ExpressionBean(String operator, String type, String category, String field, String value_type,
                          String compare, String value) {
        this.operator = operator;
        this.type = type;
        this.category = category;
        this.field = field;
        this.value_type = value_type;
        this.compare = compare;
        this.value = value;
    }

    public ExpressionBean(List<ExpressionBean> subExpression) {
        this.subExpression = subExpression;
    }

    public ExpressionBean() {
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<ExpressionBean> getSubExpression() {
        return subExpression;
    }

    public void setSubExpression(List<ExpressionBean> subExpression) {
        this.subExpression = subExpression;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue_type() {
        return value_type;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExpressionBean{" +
                "operator='" + operator + '\'' +
                ", subExpression=" + subExpression +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", field='" + field + '\'' +
                ", value_type='" + value_type + '\'' +
                ", compare='" + compare + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
