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
public class BetweenFilterInterfaceImp extends FilterInterfaceImp {
    public static final Logger log = LoggerFactory.getLogger(BetweenFilterInterfaceImp.class);

    public static final String BETWEEN_SPLIT_CHAR = ",";

    private String fistParameter;
    private String secondParameter;

    public BetweenFilterInterfaceImp(String dataSet, String property, OperatorEnum operator,
                                     PropertyTypeEnum valueType, String value) {
        super(dataSet, property, operator, valueType, value);
        String[] tokenArr = value.trim().split(BETWEEN_SPLIT_CHAR, 2);
        if (tokenArr.length == 2) {
            fistParameter = tokenArr[0];
            secondParameter = tokenArr[1];
        } else {
            log.error("input between value error [" + value + "]");
        }
    }

    public BetweenFilterInterfaceImp() {
        super();
    }

    @Override
    public String getCondition() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(fistParameter) && StringUtils.isNotBlank(secondParameter)) {
            sb.append(property.trim());
            sb.append(" ").append(operator.getAbbreviation());
            sb.append(" [").append(ExpressionUtil.strToObject(valueType, fistParameter)).append(",")
                    .append(ExpressionUtil.strToObject(valueType, secondParameter)).append("]");
        }
        log.info("between condition: [{}]", sb.toString());
        return sb.toString();
    }

}
