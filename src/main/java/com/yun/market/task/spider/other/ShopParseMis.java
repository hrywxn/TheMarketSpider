package com.yun.market.task.spider.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.config.HttpConfig;
import com.yun.market.model.shop.ShopModel;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.service.general.GeneralService;
import com.yun.market.service.sprider.SpriderTaskService;
import com.yun.market.service.yye.LhbService;
import com.yun.market.util.TimesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShopParseMis {
    @Autowired
    SpriderTaskService spriderTaskService;
    @Autowired
    GeneralService generalService;
    @Autowired
    LhbService lhbService;

    public void doStartGeneMis(SpiderTaskMode spiderNode) {
        int id = spiderNode.getId();
        spriderTaskService.updateTaskStart(spiderNode, "shop"); //标识抓取开始

        for (int i = 1; i < 11; i++) {

            int page = (i - 1) * 60;

            String spiderHttp = spiderNode.getSpiderHttp()
                    .replace("fixOpendateHis", String.valueOf(page))
                    .replace("findKey",spiderNode.getFixOpendateHis());


            //获取内容
            String html = HttpConfig.sendGet(spiderHttp);

            JSONObject jsonObject = JSON.parseObject(html);

            JSONArray jsonArray = jsonObject.getJSONArray("items");

            List<ShopModel> shopModelList = new ArrayList<>();
            for (Object json:jsonArray){

                ShopModel shopModel = new ShopModel();

                String name = JSON.parseObject(json.toString()).getJSONObject("item_basic").getString("name");
                shopModel.setName(name);

                String sold = JSON.parseObject(json.toString()).getJSONObject("item_basic").getString("sold");
                shopModel.setSold(sold);

                String ctime = JSON.parseObject(json.toString()).getJSONObject("item_basic").getString("ctime");

                String view_count = JSON.parseObject(json.toString()).getJSONObject("item_basic").getString("view_count");
                shopModel.setView_count(view_count);

                String shop_location = JSON.parseObject(json.toString()).getJSONObject("item_basic").getString("shop_location");
                shopModel.setShop_location(shop_location);

                ctime = TimesUtil.stampToDate(ctime);
                shopModel.setCtime(ctime);

                shopModel.setType(spiderNode.getFixOpendateHis());

                double price = JSON.parseObject(json.toString()).getJSONObject("item_basic").getDouble("price")/100000;
                shopModel.setPrice(price);

                shopModelList.add(shopModel);
            }

            String saveSql = CreatedSqlCommon.initCreatedTable(ShopModel.class, "t_market_shop");

            lhbService.saveDataShop(saveSql, shopModelList);
        }

        spriderTaskService.updateTaskEnd(spiderNode, "shop"); //标识抓取开始
    }
}
