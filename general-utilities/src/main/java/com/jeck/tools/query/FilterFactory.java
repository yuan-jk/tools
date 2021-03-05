package com.jeck.tools.query;


import com.jeck.tools.query.ExpressionUtil.OperatorEnum;
import com.jeck.tools.query.ExpressionUtil.PropertyTypeEnum;

/**
 * @author yuanjk
 * @version 21/3/5
 */
public class FilterFactory {

    public static FilterInterface createFilter(String dataSet, String property, OperatorEnum operator,
                                               PropertyTypeEnum valueType, String value) {
        FilterInterface baseFilter;
        switch (operator) {
            case BetweenFilteringItem:
                baseFilter = new BetweenFilterInterfaceImp(dataSet, property, operator, valueType, value);
                break;
            case SimilarFilteringItem:
                baseFilter = new SimilarFilterInterfaceImp(dataSet, property, operator, valueType, value);
                break;
            default:
                baseFilter = new FilterInterfaceImp(dataSet, property, operator, valueType, value);
        }
        return baseFilter;
    }

}
