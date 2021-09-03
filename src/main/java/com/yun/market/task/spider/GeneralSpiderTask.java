package com.yun.market.task.spider;

import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.service.sprider.SpriderTaskService;
import com.yun.market.task.spider.general.HtmlParseMis;
import com.yun.market.task.spider.general.JsonParseMis;
import com.yun.market.task.spider.general.NewsParseMis;
import com.yun.market.task.spider.general.NoJsonParseMis;
import com.yun.market.task.spider.other.ShopParseMis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralSpiderTask{

    @Autowired
    JsonParseMis jsonParseMis;

    @Autowired
    NoJsonParseMis noJsonParseMis;

    @Autowired
    HtmlParseMis htmlParseMis;

    @Autowired
    NewsParseMis newsParseMis;

    @Autowired
    SpriderTaskService spriderTaskService;

    @Autowired
    ShopParseMis shopParseMis;

    public void execute() {
        try {
            doSpiderTask();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    private void doSpiderTask() {

        //获取需要抓取的随机一条任务
        SpiderTaskRulesModel rulesModel = spriderTaskService.getSpiderTaskRulesModel();

        if(rulesModel==null){
            return;
        }

        //获取对应的网页地址信息
        SpiderTaskMode spiderNode = spriderTaskService.getSpiderTaskMode(rulesModel.getTaskId());

        String rulesType= rulesModel.getContentType(); //规则类型

        if (rulesType.startsWith("html")){

            htmlParseMis.doStartGeneMis(rulesModel,spiderNode);

        }else if(rulesType.startsWith("news")){

            newsParseMis.doStartGeneMis(rulesModel,spiderNode);

        }else if(rulesType.startsWith("no-json")){

            noJsonParseMis.doStartGeneMis(rulesModel,spiderNode);

//        }else if(rulesType.startsWith("shop")){
//
//            shopParseMis.doStartGeneMis(rulesModel,spiderNode);

        }else{

            jsonParseMis.doStartGeneMis(rulesModel,spiderNode);

        }
    }
}
