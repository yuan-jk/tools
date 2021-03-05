package com.jeck.tools.query;

import java.util.List;

import com.jeck.tools.query.ExpressionUtil.PropertyTypeEnum;
import com.jeck.tools.query.ExpressionUtil.OperatorEnum;
import com.jeck.tools.query.ExpressionUtil.LogicalOperatorEnum;

/**
 * @author yuanjk
 * @version 21/3/4
 */
public class ExpressionBean {

    private LogicalOperatorEnum logicalOperator;
    private List<ExpressionBean> subExpression;
    private String objectTypeId;
    private String dataSet;
    private String property;
    private PropertyTypeEnum valueType;
    private OperatorEnum operator;
    private String value;

    public ExpressionBean(LogicalOperatorEnum operator, List<ExpressionBean> subExpression) {
        this.logicalOperator = operator;
        this.subExpression = subExpression;
    }

    public ExpressionBean(String objectTypeId, String dataSet, String property,
                          PropertyTypeEnum valueType, OperatorEnum operator, String value) {
        this.objectTypeId = objectTypeId;
        this.dataSet = dataSet;
        this.property = property;
        this.valueType = valueType;
        this.operator = operator;
        this.value = value;
    }

    public ExpressionBean() {
    }

    public LogicalOperatorEnum getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(LogicalOperatorEnum logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public List<ExpressionBean> getSubExpression() {
        return subExpression;
    }

    public void setSubExpression(List<ExpressionBean> subExpression) {
        this.subExpression = subExpression;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public PropertyTypeEnum getValueType() {
        return valueType;
    }

    public void setValueType(PropertyTypeEnum valueType) {
        this.valueType = valueType;
    }

    public OperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
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
                "logicalOperator='" + logicalOperator + '\'' +
                ", subExpression=" + subExpression +
                ", objectTypeId='" + objectTypeId + '\'' +
                ", dataSet='" + dataSet + '\'' +
                ", property='" + property + '\'' +
                ", valueType=" + valueType +
                ", operator=" + operator +
                ", value='" + value + '\'' +
                '}';
    }
}
