package com.jeck.tools.query;

import com.jeck.tools.query.ExpressionUtil.OperatorEnum;
import com.jeck.tools.query.ExpressionUtil.PropertyTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanjk
 * @version 21/3/5
 */
public class SimilarFilterInterfaceImp extends FilterInterfaceImp {
    public static final Logger log = LoggerFactory.getLogger(SimilarFilterInterfaceImp.class);

    public SimilarFilterInterfaceImp(String dataSet, String property, OperatorEnum operator,
                                     PropertyTypeEnum valueType, String value) {
        super(dataSet, property, operator, valueType, value);
    }

    public SimilarFilterInterfaceImp() {
        super();
    }

    @Override
    public String getCondition() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(value)) {
            sb.append(property.trim());
            sb.append(" ").append(operator.getAbbreviation());
            sb.append(" '%").append(value.trim()).append("%'");
        }
        log.info("like condition: [{}]", sb.toString());
        return sb.toString();
    }

}
