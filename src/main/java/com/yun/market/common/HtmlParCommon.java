package com.yun.market.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class HtmlParCommon {

    public static List<Map<String, String>> doParsinHtmlToContent(String html, String formatting, String rules, Map<String, String> kvMap) {

        String[] ruleArr = rules.split("->");

        List<Map<String, String>> ruleListMap = new LinkedList<>();

        //规则类
        String valType = ruleArr[0];

        //转移Obj 、 arr
        String val = ruleArr[1];

        Elements elements = null;

        //判断上一次的类型,和规则
        if (valType.startsWith("byClass")) {

            elements = getHtmlByClassEs(html, val);

        } else if (valType.startsWith("bySelect")) {

            elements = getHtmlBySelectEs(html, val);

        }

        for (Element element : elements) {

            try {

                html = element.toString();

                String repStr = String.format("%s->%s->", valType, val);

                rules = rules.replace(repStr, "");

                List<Map<String, String>> kvList = parsingHtmlToContent(html, formatting, rules, kvMap);

                addToList(kvList, ruleListMap);

            } catch (Exception e) {
                System.out.println(e);
            }

        }

        return ruleListMap;

    }

    /**
     * 获取对应的Class数量
     *
     * @param html
     * @param rule
     * @return
     */
    private static Elements getHtmlByClassEs(String html, String rule) {

        Elements select = Jsoup.parse(html).getElementsByClass(rule);

        return select;
    }

    /**
     * 获取对应的Select数量
     *
     * @param html
     * @param rule
     * @return
     */
    private static Elements getHtmlBySelectEs(String html, String rule) {

        Elements jsonNode = Jsoup.parse(html).select(rule);

        return jsonNode;
    }

    /**
     * 添加到list
     *
     * @param kvList
     * @param ruleListMap
     */
    private static void addToList(List<Map<String, String>> kvList, List<Map<String, String>> ruleListMap) {

        for (Map<String, String> map : kvList) {
            ruleListMap.add(map);
        }

    }

    /**
     * 获取标准化html，返回结果集
     *
     * @param html  传入的html内容
     * @param rules 解析需要的html内容规则
     * @param kvMap
     * @return
     */
    public static List<Map<String, String>> parsingHtmlToContent(String html, String formatting, String rules, Map<String, String> kvMap) {

        String[] ruleArr = rules.split("->");

        List<Map<String, String>> ruleListMap = new LinkedList<>();

        String valType = "";

        for (int i = 0; i < ruleArr.length; i++) {

            //转移Obj 、 arr
            String val = ruleArr[i];

            if (i % 2 == 0) {   //遇到分隔符则返回

                valType = val;

                continue;
            }

            //判断上一次的类型,和规则
            if (valType.startsWith("byClass")) {

                html = getHtmlByClass(html, val).toString();

            } else if (valType.startsWith("bySelect")) {

                html = getHtmlBySelect(html, val).toString();

            } else if (valType.startsWith("byTr")) {

                ruleListMap = getHtmlByTr(html, formatting, val, kvMap);

            }
        }
        return ruleListMap;
    }

    /**
     * 通过calss标签获取返回值
     *
     * @param html
     * @param rule
     * @return
     */
    private static Element getHtmlByClass(String html, String rule) {

        Element jsonNode = null;

        if (rule.contains("getIndex")) {

            String ruleVal = rule.split(":")[0];

            int index = Integer.parseInt(rule.split(":")[1].split("=")[1]);

            Elements elements = Jsoup.parse(html).getElementsByClass(ruleVal);

            if (elements.size() > 0) {

                jsonNode = elements.get(index);

            }

        } else { //查看规则是否有取值index

            Elements select = Jsoup.parse(html).getElementsByClass(rule);

            jsonNode = select.get(0);

        }

        return jsonNode;
    }

    /**
     * 通过Tr标签获取返回值
     *
     * @param html
     * @param formatting
     * @param rule
     * @param kvMap
     * @return
     */
    private static List<Map<String, String>> getHtmlByTr(String html, String formatting, String rule, Map<String, String> kvMap) {

        html = html.replace("<tbody", "<table").replace("</tbody", "</table");

        Elements elements = Jsoup.parse(html).select("tr");

        List<Map<String, String>> addMapList = new ArrayList<>();

        String[] array = rule.split(",");

        for (Element element : elements) {

            Map<String, String> addMap = new HashMap<>();

            for (String index : array) {

                Element ele = element.child(Integer.valueOf(index));

                String result = ele.text();

                result = RulesCommon.initFormatting(formatting, result);

                String key = kvMap.get(index);

                addMap.put(key, result);

            }

            addMapList.add(addMap);

        }

        return addMapList;
    }

    /**
     * 通过选择标签获取返回值
     *
     * @param html
     * @param rule
     * @return
     */
    private static Element getHtmlBySelect(String html, String rule) {

        Element jsonNode = null;

        if (rule.contains("getIndex")) {

            String ruleVal = rule.split(":")[0];

            int index = Integer.parseInt(rule.split(":")[1].split("=")[1]);

            Elements elements = Jsoup.parse(html).select(ruleVal);

            if (elements.size() > 0) {

                jsonNode = Jsoup.parse(html).select(ruleVal).get(index);

            }

        } else { //查看规则是否有取值index

            jsonNode = Jsoup.parse(html).select(rule).get(0);

        }

        return jsonNode;
    }

}
