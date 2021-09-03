package com.yun.market.common;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RulesCommon {

    /**
     * 初始化映射字段
     * @param mappingColumn
     * @return
     */
    public static Map<String, String> iniKeyValueMap(String mappingColumn) {

        Map<String, String> map = new HashMap();

        String [] mappingArray = mappingColumn.split(",");

        for (String mapping : mappingArray){;

            String[] kvMapping = mapping.split(":");

            map.put(kvMapping[0],kvMapping[1]);
        }

        map.put("opendate","opendate");

        return map;
    }

    /**
     * 将内容json标准化
     *
     * @param content
     * @param repBefore
     * @return
     */
    public static String displaceServcie(String content, String repBefore) {

        if (StringUtils.isEmpty(repBefore)){
            return content;
        }

        String[] splitArr = repBefore.split(",");

        //replace content
        for (String val : splitArr) {
            content = content.replace(val, "=");
        }

        //standard content
        String[] standardCon = content.split("=");

        return standardCon[1];
    }

    /**
     * 标准化kv内容取值
     * @param formatting
     * @param reVal
     * @return
     */
    public static String initFormatting(String formatting, String reVal) {

        if (StringUtils.isEmpty(formatting)){
            return reVal;
        }

        String [] formatArray = formatting.split(",");

        for (String format : formatArray){
            reVal = reVal.replace(format,"");
        }

        return reVal;
    }

}
