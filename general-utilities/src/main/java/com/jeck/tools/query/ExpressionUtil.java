package com.jeck.tools.query;

/**
 * @author yuanjk
 * @version 21/3/5
 */
public class ExpressionUtil {


    public static String strToObject(PropertyTypeEnum type, String value) {
        String rs;
        switch (type) {
            case STRING:
                rs = "'" + value + "'";
                break;
            default:
                rs = value;
        }
        return rs;
    }


    enum PropertyTypeEnum {
        SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN, STRING, TIMESTAMP
    }

    enum OperatorEnum {
        BetweenFilteringItem("BETWEEN"),
        EqualFilteringItem("="),
        GreaterThanFilteringItem(">="),
        GreaterThanEqualFilteringItem("<"),
        LessThanFilteringItem("<"),
        LessThanEqualFilteringItem("<="),
        NotEqualFilteringItem("!="),
        SimilarFilteringItem("LIKE");

        private String abbreviation;

        public String getAbbreviation() {
            return abbreviation;
        }

        private OperatorEnum(String abb) {
            this.abbreviation = abb;
        }
    }

    enum LogicalOperatorEnum {
        AND, OR
    }


}

