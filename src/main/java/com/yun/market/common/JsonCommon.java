package com.yun.market.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonCommon {

    /**
     * 解析规则化后json数据，返回结果集
     *
     * @param content
     * @param rule
     * @param splitRule
     * @param kvMap
     * @return
     */
    public static List<Map<String, String>> parsingJsonMap(String content, String rule, String splitRule, String formatting, Map<String, String> kvMap) {

        //分解解析规则
        String[] ruleArr = rule.split("->");

        List<Map<String, String>> ruleListMap = new LinkedList<>();

        Map<String, String> resultVal = new HashMap<>();

        String valType = "";

        for (int i = 0; i < ruleArr.length; i++) {
            //转移Obj 、 arr
            String val = ruleArr[i]; //规则
            if (i % 2 == 0) {
                valType = val;
                continue;
            }

            //反推现在的结果是否取值，也就是是否包含text
            if (val.contains("text")) {

                //判断取值的类型
                if (valType.startsWith("getObj")) {

                    //取值Object时，判断取值是否多个
                    val = val.replace("text:", ""); //先把text替换万

                    String[] valResult = val.split(","); //先把text替换万

                    if (StringUtils.isEmpty(splitRule) || splitRule.equals("none")) {
                        for (String valStr : valResult) {

                            content = parsingGetObject(content, valStr);

                            resultVal.put(valStr, content);
                        }

                        ruleListMap.add(resultVal);

                        return ruleListMap;

                    } else {

                        //先分大，后分小  专为指数抓取兼任
                        String[] ruleZs = splitRule.split("|");

                        String[] contentZs = content.split(ruleZs[0]);

                        for (String cot : contentZs) {

                            String[] cotArr = cot.split(ruleZs[2]);

                            resultVal = new HashMap<>();

                            for (String valZs : valResult) {

                               String result = cotArr[Integer.parseInt(valZs)];

                               String key = kvMap.get(valZs);

                                resultVal.put(key, result);
                            }

                            ruleListMap.add(resultVal);

                        }

                        return ruleListMap;
                    }

                } else {

                    //将arr下的数据返回到map中
                    ruleListMap = parsingGetReturnArray(content, val, splitRule, formatting, kvMap);

                    return ruleListMap;
                }
            }

            //如果还是不是取值的情况下
            if (valType.startsWith("getObj")) {

                content = parsingGetObject(content, val);

            } else if ("getArr".equals(valType)) {

                //将arr下的数据返回到map中
                content = parsingGetArray(content, val);

            }
        }
        return ruleListMap;
    }

    /**
     * 返回Array结果列表
     *
     * @param content
     * @param val
     * @param splitRule
     * @param kvMap
     * @return
     */
    private static List<Map<String, String>> parsingGetReturnArray(String content, String val, String splitRule, String formatting, Map<String, String> kvMap) {

        List<Map<String, String>> ruleListMap = new LinkedList<>();

        JSONArray jsonData = JSON.parseArray(content);

        for (int i = 0; i < jsonData.size(); i++) {

            String value = jsonData.getString(i);

            String[] valSplit = val.replace("text:", "").split(",");

            String[] arrContent = value.replace(splitRule, "=").split("="); //非标准结果返回分割

            Map<String, String> resultVal = new HashMap<>();

            for (String valStr : valSplit) {

                int flagIndex = Integer.parseInt(valStr); //转化取值序号

                String result = arrContent[flagIndex];

                result = RulesCommon.initFormatting(formatting, result);

                String key = kvMap.get(String.valueOf(flagIndex));

                resultVal.put(key, result);
            }

            ruleListMap.add(resultVal);
        }

        return ruleListMap;
    }

    /**
     * 返回array取值
     *
     * @param content
     * @param val
     * @return
     */
    private static String parsingGetArray(String content, String val) {

        JSONArray jsonData = JSON.parseArray(content);

        int flagIndex = Integer.parseInt(val.replace("getIndex:", ""));

        return jsonData.getString(flagIndex);
    }

    /**
     * 返回object取值
     *
     * @param content
     * @param val
     * @return
     */
    private static String parsingGetObject(String content, String val) {

        Object jsonData = JSON.parseObject(content).get(val);

        return jsonData.toString();
    }

}
