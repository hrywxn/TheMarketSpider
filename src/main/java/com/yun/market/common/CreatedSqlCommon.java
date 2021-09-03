package com.yun.market.common;

import java.lang.reflect.Field;
import java.util.Map;

public class CreatedSqlCommon {

    /**
     * 初始化数据保存
     * @param tableName
     * @return
     */
    public static String initCreatedTable(Class<?>t,String tableName) {

        Field[] clunms = t.getDeclaredFields();

        String sqlCommon = "Insert INTO " + tableName + "(";

        int index = 0;

        for (Field f : clunms) {

            String name = f.getName();

            index++;

            //仅有一个参数或是最后一个参数
            if ((index == 1 && clunms.length == 1) || clunms.length == index) {
                sqlCommon += String.format("`%s`)", name);
            } else {
                //Constants.SQL.ADD 和 and是一样的
                //sqlCommon += key+"="+value+" "+ Constants.SQL.ADD +" ";
                sqlCommon += String.format("`%s`,", name);
            }
        }

        sqlCommon = String.format("%s VALUES(", sqlCommon);

        index = 0;

        for (Field f : clunms) {

            String name = f.getName();

            index++;

            if ((index == 1 && clunms.length == 1) || clunms.length == index) {
                sqlCommon += String.format(":%s)", name);
            } else {
                sqlCommon += String.format(":%s,", name);
            }
        }

        return sqlCommon;
    }

    /**
     * 初始化保存sql
     * @param tableName
     * @param kvMap
     * @return
     */
    public static String iniSaveSql(String tableName, Map<String, String> kvMap) {

        String sql = String.format("insert into %s (", tableName);

        String values = "values (";

        for (String column : kvMap.values()) {

            sql = sql + String.format("`%s`,", column);

            values = values + String.format(":%s,", column);
        }

        sql = sql + "})";

        sql = sql.replace(",}", "");

        values = values + "})";

        values = values.replace(",}", "");

        return String.format("%s%s", sql, values);
    }

}
