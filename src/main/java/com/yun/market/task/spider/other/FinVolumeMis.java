package com.yun.market.task.spider.other;

import com.yun.market.common.ParsingHttp;
import com.yun.market.config.HttpConfig;
import com.yun.market.model.FoxxcodeMode;
import com.yun.market.model.quotation.QuotationFinVolumeModel;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.service.foxxcode.QuotationFinVolumeService;
import com.yun.market.service.sprider.SpriderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FinVolumeMis {

    @Autowired
    SpriderTaskService spriderTaskService;

    @Autowired
    QuotationFinVolumeService quotationFinVolumeService;

    @Async
    public void doFoxxCodeMis(SpiderTaskMode spiderNode, List<SpiderTaskParameterMode> parameterModeList, List<FoxxcodeMode> queryFoxxList) {

        if (!spiderNode.getDescribe().contains("爬取融资融卷")) {
            return;
        }

        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 20); //标识抓取开始

        queryFoxxList.stream().forEach(x -> {

            System.out.println("=========融资融卷下载：" + x.getFoxxcode() + " ============");

            String scode = String.format("scode=%s", x.getFoxxcode()); //x.getFoxxcode()
            String content = spiderNode.getSpiderHttp().replace("scode=603259", scode);
            //获取网页内容
            String html = HttpConfig.doGet(content, parameterModeList);

            String repBefore = "(;)"; //替换前多个用;隔开
            String repAfter = "="; //替换成
            int index = 1;
            String repHtml = ParsingHttp.displaceServcie(html, repBefore, repAfter, index);

            if (repHtml.contains("返回数据为空")) {
                return;
            }

            List<Map<String, String>> resultVal = ParsingHttp.parsingMap(repHtml,
                    "obj->result->obj->data->arr->text:DATE,text:SPJ," +
                            "text:RZYE,text:SCODE,text:SECNAME");
            callSave(resultVal);

            List<Map<String, String>> pagMap = ParsingHttp.parsingMap(repHtml, "obj->result->obj->text:pages");
            int pages = Integer.parseInt(pagMap.get(0).get("pages")); //定义总页数

            for (int i = 2; i < pages; i++) {
                String rePages = String.format("p=%s", i);
                String contentRep = content.replace("p=1", rePages);
                html = HttpConfig.doGet(contentRep, parameterModeList);

                repBefore = "(;)"; //替换前多个用;隔开
                repAfter = "="; //替换成
                index = 1;
                repHtml = ParsingHttp.displaceServcie(html, repBefore, repAfter, index);

                if (repHtml.contains("返回数据为空")) {
                    return;
                }

                resultVal = ParsingHttp.parsingMap(repHtml,
                        "obj->result->obj->data->arr->text:DATE,text:SPJ," +
                                "text:RZYE,text:SCODE,text:SECNAME");
                callSave(resultVal);
            }
        });
        System.out.println(String.format("============抓取股票代码任务完成============"));
        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 80); //标识抓取开始
    }

    private void callSave(List<Map<String, String>> resultValList) {

        List<QuotationFinVolumeModel> list = new ArrayList<>();
        for (Map<String, String> resultVal : resultValList) {

            QuotationFinVolumeModel finVolumeModel = new QuotationFinVolumeModel();
            finVolumeModel.setOpendate(Integer.parseInt(resultVal.get("DATE")
                    .replace("-", "")
                    .replace(" 00:00:00", "")));
            finVolumeModel.setFoxxcode(resultVal.get("SCODE"));
            finVolumeModel.setName(resultVal.get("SECNAME"));
            finVolumeModel.setRzye(Long.parseLong(resultVal.get("RZYE")));
            finVolumeModel.setClose(Double.parseDouble(resultVal.get("SPJ")));
            list.add(finVolumeModel);
        }

        quotationFinVolumeService.saveDataDo(list);
    }
}
