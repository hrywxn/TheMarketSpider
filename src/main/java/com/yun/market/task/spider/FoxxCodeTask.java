package com.yun.market.task.spider;

import com.yun.market.model.FoxxcodeMode;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.service.foxxcode.FoxxcodeService;
import com.yun.market.service.sprider.SpriderTaskService;
import com.yun.market.task.spider.other.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoxxCodeTask {

    @Autowired
    SpriderTaskService spriderTaskService;

    @Autowired
    FoxxcodeService foxxcodeService;

    @Autowired
    FoxxCodeMis foxxCodeMis;

    @Autowired
    QuotationDayMis quotationDayMis;

    @Autowired
    FinVolumeMis finVolumeMis;

    @Autowired
    YyeMis yyeMis;

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

        List<FoxxcodeMode> queryFoxxList = foxxcodeService.getFoxxcodeList();

        //获取需要抓取的随机一条任务
        SpiderTaskMode spiderNode = spriderTaskService.getSpiderTaskMode();

        if(spiderNode==null){
           return;
        }

        //查找对应的任务参数
        List<SpiderTaskParameterMode> parameterModeList = spriderTaskService.getSpiderTaskParaMode(spiderNode.getId());
        //查询对应定时任务
        List<SpiderTaskRulesModel> spiderTaskRulesModelList = spriderTaskService.getSpiderTaskRulesMode(spiderNode.getId());

        foxxCodeMis.doFoxxCodeMis(spiderNode,parameterModeList,queryFoxxList); //爬取股票代码

        quotationDayMis.doFoxxCodeMis(spiderNode,parameterModeList,queryFoxxList);//爬取行情

        finVolumeMis.doFoxxCodeMis(spiderNode,parameterModeList,queryFoxxList); //爬取融资融卷

        yyeMis.doFoxxCodeMis(spiderNode,parameterModeList,queryFoxxList); //爬取营业额

        shopParseMis.doStartGeneMis(spiderNode);//爬取商品

    }

}
