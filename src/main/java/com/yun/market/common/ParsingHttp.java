package com.yun.market.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.yye.LhbNodeModel;
import com.yun.market.model.yye.YybNodeModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class ParsingHttp {
    /**
     * 将内容json标准化
     *
     * @param content
     * @param repBefore
     * @param repAfter
     * @return
     */
    public static String displaceServcie(String content, String repBefore, String repAfter, int index) {
        String[] splitArr = repBefore.split(";");
        //replace content
        for (String val : splitArr) {
            content = content.replace(val, repAfter);
        }

        //standard content
        String[] standardCon = content.split(repAfter);
        return standardCon[index];
    }

    public static List<Map<String, String>> parsingMap(String content, String rule) {
        //分解解析规则
        String[] ruleArr = rule.split("->");

        List<Map<String, String>> ruleListMap = new LinkedList<>();
        Map<String, String> resultVal = new HashMap<>();

        String valType = "";
        for (int i = 0; i < ruleArr.length; i++) {
            //转移Obj 、 arr
            String val = ruleArr[i];
            if (i % 2 == 0) {
                valType = val;
                continue;
            }

            //解析规则取值
            String[] valueRuleArr = val.split(";");

            for (String valueVal : valueRuleArr) {

                //判断上一次的类型
                if (valType.startsWith("obj")) {

                    //转化内容
                    if (valueVal.contains("text")) {
                        valueVal = valueVal.replace("text:", "");
                        resultVal.put(valueVal, parsingObject(content, valueVal));
                        ruleListMap.add(resultVal);
                        continue;
                    }

                    content = parsingObject(content, valueVal);
                } else {
                    //将arr下的数据返回到map中
                    ruleListMap = parsingArray(content, valueVal, resultVal, ruleListMap);
                    return ruleListMap;
                }
            }
        }
        return ruleListMap;
    }

    private static String parsingObject(String content, String val) {
        Object jsonData = JSON.parseObject(content).get(val);
        return jsonData.toString();
    }

    private static List<Map<String, String>> parsingArray(String content, String val, Map<String, String> resultMapVal, List<Map<String, String>> ruleListMap) {
        JSONArray jsonData = JSON.parseArray(content);

        if (val.equals("text")) {
            for (int i = 0; i < jsonData.size(); i++) {
                resultMapVal.put(String.valueOf(i), jsonData.get(i).toString());
            }
            ruleListMap.add(resultMapVal);
        }else if (val.equals("getIndex")){
            resultMapVal.put("1", jsonData.toString());
            ruleListMap.add(resultMapVal);
        }else {
            for (int i = 0; i < jsonData.size(); i++) {
                String arrContent = jsonData.getString(i);
                String[] valSplit = val.split(",");
                resultMapVal = new HashMap<>();
                for (String vsp : valSplit) {
                    vsp = vsp.replace("text:", "");
                    String reVal = parsingObject(arrContent, vsp);

                    resultMapVal.put(vsp, reVal);
                }
                ruleListMap.add(resultMapVal);
            }
        }
        return ruleListMap;
    }

    public static List<SpiderTaskParameterMode> initHttpHeader(SpiderTaskMode spiderNode) {

        List<SpiderTaskParameterMode> parameterModeList = new ArrayList<>();

        String headerVal = spiderNode.getSpiderHeader();

        String[] headerValArray = headerVal.split("\r\n");

        for (String header : headerValArray) {
            String[] kvHeaderArray = header.split(": ");
            SpiderTaskParameterMode parameterMode = new SpiderTaskParameterMode();
            parameterMode.setRawKey(kvHeaderArray[0]);
            parameterMode.setRawValue(kvHeaderArray[1]);
            parameterModeList.add(parameterMode);
        }

        return parameterModeList;
    }

    public static List<LhbNodeModel> parseLhbNodeHtml(String htmlContent) {
        Elements elements = Jsoup.parse(htmlContent).getElementsByClass("twrap");

        Elements tableNodes = elements.get(0).select("table").select("tr");

        List<LhbNodeModel> list = new LinkedList<>();
        for (Element element : tableNodes) {
            LhbNodeModel lhb = new LhbNodeModel();
            lhb.setCode(element.childNode(3).childNode(0).toString());
            lhb.setRatio(element.childNode(9).childNode(0).toString());
            lhb.setJmr(element.childNode(13).childNode(0).toString());
            lhb.setStkname(element.childNode(5).childNode(0).childNode(0).toString());
            lhb.setPrice(element.childNode(7).childNode(0).toString());
            lhb.setRatio(element.childNode(9).childNode(0).toString().replace("%",""));
            list.add(lhb);
        }

        return list;
    }

    //获取明细值
    public static List<String> getLhbCom(String htmlContent) {
        Elements elements = Jsoup.parse(htmlContent).getElementsByTag("p");

        List<String> list = new ArrayList<>();
        for (Element element : elements) {
            String ele = element.childNode(0).toString();
            if (!ele.contains("明细")) {
                continue;
            }
            list.add(ele);
        }
        return list;
    }

    public static List<YybNodeModel> getLhbYyb(String htmlContent) {
        Elements elements = Jsoup.parse(htmlContent).getElementsByClass("stockcont");

        List<YybNodeModel> yybNodeModelList = new LinkedList<>();

        for (Element element : elements) {
            String stockcode = element.attr("stockcode");
            String buy = Jsoup.parse(element.toString()).select("span").tagName("c-rise").get(0).childNode(0).toString();
            String sell = Jsoup.parse(element.toString()).select("span").tagName("c-fall").get(1).childNode(0).toString();

            double buyAmount = Double.parseDouble(buy.replace("\n", "").replace(" ", ""));
            double sellAmount = Double.parseDouble(sell.replace("\n", "").replace(" ", ""));
            double allAmount = sellAmount + buyAmount;

            Elements tableNodes = element.select("table").select("tr");

            String flagSell = "";
            for (Element table : tableNodes) {

                String tl = table.getElementsByClass("tl").text();
                if (tl.contains("买入金额最大")) {
                    flagSell = "买入";
                    continue;
                }
                if (tl.contains("卖出金额最大")) {
                    flagSell = "卖出";
                    continue;
                }

                YybNodeModel yybNode = new YybNodeModel();
                yybNode.setFoxxcode(stockcode);
                yybNode.setAllamount(allAmount);

                String com = table.select("a").attr("title");
                yybNode.setCom(com);

                if (flagSell.equals("买入")) {
                    buyAmount = Double.parseDouble(table.getElementsByClass("c-rise tr rel").get(0).text()); //买入
                    yybNode.setBuyamount(buyAmount); //等于a
                    //卖出
                    Elements sells = table.getElementsByClass("c-fall tr rel");
                    yybNode.setSellamount(0.000);
                    yybNode.setNetamount(buyAmount - sellAmount); //等于a
                } else {
                    //买出
                    Elements buys = table.getElementsByClass("c-rise tr rel");

                    yybNode.setBuyamount(0.000);
                    sellAmount = Double.parseDouble(table.getElementsByClass("c-fall tr rel").get(0).text());
                    yybNode.setSellamount(sellAmount); //等于a

                    yybNode.setNetamount(buyAmount - sellAmount); //等于a
                }

                yybNodeModelList.add(yybNode);
            }
        }
        return yybNodeModelList;
    }

}
