package com.yun.market.parsing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yun.market.model.FoxxcodeMode;
import com.yun.market.model.quotation.QuotationDayModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FoxxCodeParsing {

    public static int getPageCount(String html) {
        JSONObject jsonObject = JSON.parseObject(html);
        int pagecount = jsonObject.getIntValue("pagecount");
        return pagecount;
    }

    public static int getPages(String html) {

        Document document = Jsoup.parse(html);

        Elements postItems = document.getElementsByClass("page_info");

        String pages = postItems.get(0).childNode(0).toString().replace("1/", "");

        return Integer.parseInt(pages);
    }

    public static Map<String, String> getFoxxcode(String html) {

        Map<String, String> map = new HashMap<>();

        Document document = Jsoup.parse(html);

        Elements links = document.select("a[href]a[target=_blank]");

        String foxxcode = "";
        String name = "";
        for (int i = 0; i < links.size(); i++) {
            if (i % 2 != 0) {
                name = links.get(i).childNode(0).toString();
                map.put(foxxcode, name);
            } else {
                foxxcode = links.get(i).childNode(0).toString();
            }
        }
        return map;
    }

    public static List<FoxxcodeMode> getFoxxcodeList(String html, List<FoxxcodeMode> queryFoxxList) {

        String datas = JSON.parseObject(html).getString("data");

        JSONArray jsonArray = JSON.parseArray(JSON.parseObject(datas).getString("diff"));

        List<FoxxcodeMode> foxxcodeModeList = new ArrayList<>();

        for(Object json:jsonArray){
            FoxxcodeMode foxxcodeMode = new FoxxcodeMode();
            foxxcodeMode.setFoxxcode(JSON.parseObject(json.toString()).getString("f12"));
            foxxcodeMode.setName(JSON.parseObject(json.toString()).getString("f14"));

            List<String> foxx = queryFoxxList.stream().map(x->x.getFoxxcode()).collect(Collectors.toList());

            if (foxx.contains(foxxcodeMode.getFoxxcode())) {
                continue;
            }
            foxxcodeModeList.add(foxxcodeMode);
        }
        return foxxcodeModeList;
    }

    public static List<QuotationDayModel> getQuotationList(String html, List<FoxxcodeMode> queryFoxxList) {

        String datas = JSON.parseObject(html).getString("data");

        JSONArray jsonArray = JSON.parseArray(JSON.parseObject(datas).getString("diff"));

        List<QuotationDayModel> foxxcodeModeList = new ArrayList<>();

        for(Object json:jsonArray){
            QuotationDayModel foxxcodeMode = new QuotationDayModel();

//            foxxcodeMode.setFoxxcode(JSON.parseObject(json.toString()).getString("f12"));
//            foxxcodeMode.setOpendate(Integer.parseInt(StrToDate(item[0])));
//            foxxcodeMode.setOpen(JSON.parseObject(json.toString()).getString("f17"));
//            foxxcodeMode.setHigh(JSON.parseObject(json.toString()).getString("f15"));
//            foxxcodeMode.setLow(JSON.parseObject(json.toString()).getString("f16"));
//            foxxcodeMode.setClose(Double.parseDouble(item[3]));
//            foxxcodeMode.setPre_close(Double.parseDouble(item[7]));
//            foxxcodeMode.setRise_amount(item[8]);
//            foxxcodeMode.setRise_applies(item[9]);
//            foxxcodeMode.setVol(item[11]);
//            foxxcodeMode.setAmout(item[12]);
//            foxxcodeMode.setSz_value(item[13]);
//            foxxcodeMode.setLt_value(item[14]);

            foxxcodeModeList.add(foxxcodeMode);
        }
        return foxxcodeModeList;
    }
}
