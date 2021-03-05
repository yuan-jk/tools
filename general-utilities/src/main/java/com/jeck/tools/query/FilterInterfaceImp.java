package com.jeck.tools.query;

import com.jeck.tools.query.ExpressionUtil.PropertyTypeEnum;
import com.jeck.tools.query.ExpressionUtil.OperatorEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanjk
 * @version 21/3/5
 */
public class FilterInterfaceImp implements FilterInterface {
    public static final Logger log = LoggerFactory.getLogger(FilterInterfaceImp.class);
    protected String dataSet;
    protected String property;
    protected OperatorEnum operator;
    protected PropertyTypeEnum valueType;
    protected String value;

    public FilterInterfaceImp(String dataSet, String property, OperatorEnum operator, PropertyTypeEnum valueType,
                              String value) {
        this.dataSet = dataSet;
        this.property = property;
        this.operator = operator;
        this.valueType = valueType;
        this.value = value;
    }

    public FilterInterfaceImp() {
    }

    @Override
    public String getCondition() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(property)) {
            sb.append(property.trim());
            sb.append(" ").append(operator.getAbbreviation());
            sb.append(" ").append(ExpressionUtil.strToObject(valueType, value));
        }
        log.info("base condition: [{}]", sb.toString());
        return sb.toString();
    }
}
