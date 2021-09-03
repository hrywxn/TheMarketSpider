package com.yun.market.task.spider.other;

import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.common.ParsingHttp;
import com.yun.market.config.HttpConfig;
import com.yun.market.model.FoxxcodeMode;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.yye.LhbNodeModel;
import com.yun.market.model.yye.YybNodeModel;
import com.yun.market.service.general.GeneralService;
import com.yun.market.service.sprider.SpriderTaskService;
import com.yun.market.service.yye.LhbService;
import com.yun.market.util.TimesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class YyeMis {

    @Autowired
    SpriderTaskService spriderTaskService;
    @Autowired
    GeneralService generalService;
    @Autowired
    LhbService lhbService;

    @Async
    public void doFoxxCodeMis(SpiderTaskMode spiderNode, List<SpiderTaskParameterMode> parameterModeLists, List<FoxxcodeMode> queryFoxxList) {
//        if (!spiderNode.getDescribe().contains("抓取营业额")) {
//            return;
//        }
//        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 20); //标识抓取开始
//
//        //标准化请求头
//        List<SpiderTaskParameterMode> parameterModeList = ParsingHttp.initHttpHeader(spiderNode);
//
//        for (int i = 1; i < 500; i++) {
//
//            try {
//                String oldTime = TimesUtil.oldThreeToFormart(new Date(), i * -1);
//
//                String spiderHttp = spiderNode.getSpiderHttp().replace("fixOpendateHis", oldTime);
//
//                //获取内容
//                String html = HttpConfig.doGet(spiderHttp, parameterModeList);
//
//                //返回龍虎榜基本信息
//                List<LhbNodeModel> contentNode = ParsingHttp.parseLhbNodeHtml(html);
//
//                //获取明细偏离
//                List<String> comNode = ParsingHttp.getLhbCom(html);
//
//                //返回營業部基本信息
//                List<YybNodeModel> yybNode = ParsingHttp.getLhbYyb(html);
//
//                //保存数据
////                doSaveAction(contentNode, spiderNode.getFixOpendateHis(), comNode, yybNode);
//                doSaveAction(contentNode, oldTime, comNode, yybNode);
//
//                spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 80); //标识抓取开始
//            } catch (Exception e) {
//                System.out.println(e.toString());
//            }
//        }
        if (!spiderNode.getDescribe().contains("抓取营业额")) {
            return;
        }
        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 20); //标识抓取开始

        //标准化请求头
        List<SpiderTaskParameterMode> parameterModeList = ParsingHttp.initHttpHeader(spiderNode);

        String spiderHttp = spiderNode.getSpiderHttp().replace("fixOpendateHis", spiderNode.getFixOpendateHis());

        //获取内容
        String html = HttpConfig.doGet(spiderHttp, parameterModeList);

        //返回龍虎榜基本信息
        List<LhbNodeModel> contentNode = ParsingHttp.parseLhbNodeHtml(html);

        //获取明细偏离
        List<String> comNode = ParsingHttp.getLhbCom(html);

        //返回營業部基本信息
        List<YybNodeModel> yybNode = ParsingHttp.getLhbYyb(html);

        //保存数据
        doSaveAction(contentNode, spiderNode.getFixOpendateHis(), comNode, yybNode);

        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 80); //标识抓取开始
    }

    private void doSaveAction(List<LhbNodeModel> contentNode, String fixOpendateHis, List<String> comNode, List<YybNodeModel> yybNode) {

        String date = TimesUtil.DateToStr(new Date());

        if (!StringUtils.isEmpty(fixOpendateHis) && !fixOpendateHis.equals("none")) {
            date = fixOpendateHis.replace("-", "").replace("/", "");
        }

        String saveSql = CreatedSqlCommon.initCreatedTable(YybNodeModel.class, "t_market_yyb");

        List<YybNodeModel> yybNodeModelList = new ArrayList<>();

        for (YybNodeModel yyb : yybNode) {
            LhbNodeModel lhbNode = contentNode.stream().filter(x -> x.getCode().equals(yyb.getFoxxcode())).findAny().orElse(null);
            yyb.setOpendate(date); //时间
            yyb.setPrice(Double.parseDouble(lhbNode.getPrice())); //价格
            yyb.setName(lhbNode.getStkname()); //名称
            yyb.setRatio(Double.parseDouble(lhbNode.getRatio())); //名称
            yyb.setIncrease(Double.parseDouble(lhbNode.getRatio().replace("%", ""))); //涨跌幅

            String type = comNode.stream().filter(x -> x.contains(yyb.getFoxxcode())).findAny().orElse(null); //类型
            String typeRep = String.format("%s(%s)明细", lhbNode.getStkname(), yyb.getFoxxcode());
            type = type.replace(typeRep, "").replace("：", "");
            yyb.setType(type);

            yybNodeModelList.add(yyb);
        }

        //先清除所有
        date = String.valueOf(TimesUtil.getTodayCurrentTimeMillis());
        lhbService.delData(date);

        lhbService.saveData(saveSql, yybNodeModelList);

        System.out.println("======================结束====================");
    }
}