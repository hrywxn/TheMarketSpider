package com.yun.market.task.spider.other;

import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.config.HttpConfig;
import com.yun.market.model.FoxxcodeMode;
import com.yun.market.model.quotation.QuotationDayModel;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.parsing.FoxxCodeParsing;
import com.yun.market.service.foxxcode.FoxxcodeService;
import com.yun.market.service.sprider.SpriderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoxxCodeMis {

    @Autowired
    SpriderTaskService spriderTaskService;

    @Autowired
    FoxxcodeService foxxcodeService;

    @Async
    public void doFoxxCodeMis(SpiderTaskMode spiderNode, List<SpiderTaskParameterMode> parameterModeList, List<FoxxcodeMode> queryFoxxList){

        if(!spiderNode.getDescribe().contains("爬取股票代码任务")){
            return;
        }

        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()),20); //标识抓取开始

        //获取网页内容
        String html = HttpConfig.doGet(spiderNode.getSpiderHttp(), parameterModeList);

        int pages = 200;

        html = splitFoxx(html);

        //获取股票代码
        List<FoxxcodeMode> foxxcodeModeList = FoxxCodeParsing.getFoxxcodeList(html, queryFoxxList);
        callSave(foxxcodeModeList);

        //获取日K
        List<QuotationDayModel> quotationDayModelList = FoxxCodeParsing.getQuotationList(html, queryFoxxList);

        //循环拿到所有的数据
        for (int i = 2; i <= pages; i++) {

            try {
                System.out.println(String.format("抓取当前第%s页，占比%s / %s",i,i,pages));

                String rePages = String.format("pn=%s",i);

                String http = spiderNode.getSpiderHttp().replace("pn=1",rePages);

                html = HttpConfig.doGet(http, parameterModeList);

                if (html.equals("[]")){
                    break;
                }

                html = splitFoxx(html);

                //股票代码
                foxxcodeModeList = FoxxCodeParsing.getFoxxcodeList(html, queryFoxxList);
                callSave(foxxcodeModeList);

                //日K

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println(String.format("============抓取股票代码任务完成============"));
        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()),80); //标识抓取开始
    }

    private String splitFoxx(String html) {
        String[] data = html.split("\\(");
        data = data[1].split("\\)");
        return data[0];
    }

    private void callSave(List<FoxxcodeMode> foxxcodeModeList) {

        String sql = CreatedSqlCommon.initCreatedTable(FoxxcodeMode.class, "t_market_foxxcode");

        foxxcodeService.saveFoxxcode(sql, foxxcodeModeList);
    }
}
