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

}

