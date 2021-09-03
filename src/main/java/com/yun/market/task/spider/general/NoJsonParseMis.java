package com.yun.market.task.spider.general;

import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.common.JsonCommon;
import com.yun.market.common.ParsingHttp;
import com.yun.market.common.RulesCommon;
import com.yun.market.config.HttpConfig;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.service.general.GeneralService;
import com.yun.market.service.sprider.SpriderTaskService;
import com.yun.market.util.TimesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class NoJsonParseMis {
    @Autowired
    SpriderTaskService spriderTaskService;
    @Autowired
    GeneralService generalService;

    public void doStartGeneMis(SpiderTaskRulesModel rulesModel, SpiderTaskMode spiderNode) {

        int id = rulesModel.getId();

        spriderTaskService.updateTaskRulesStart(id); //标识抓取开始

        //标准化请求头
        List<SpiderTaskParameterMode> parameterModeList = ParsingHttp.initHttpHeader(spiderNode);


        String date = TimesUtil.DateToStrOn(new Date());

        //修补历史数据
        if (spiderNode.getSpiderHttp().contains("fixOpendateHis")){

            if (!StringUtils.isEmpty( spiderNode.getFixOpendateHis()) && ! spiderNode.getFixOpendateHis().equals("none")) {

                date = spiderNode.getFixOpendateHis();
            }
        }

        String spiderHttp = spiderNode.getSpiderHttp().replace("fixOpendateHis",date);

        //获取内容
        String html = HttpConfig.doGet(spiderHttp, parameterModeList);


        //格式化非json为json
        String repHtml = RulesCommon.displaceServcie(html, rulesModel.getSplitValue());

        //格式化映射需要的字段
        Map<String, String> kvMap = RulesCommon.iniKeyValueMap(rulesModel.getMappingColumn());

        //需要分割规则
        String splitRule = rulesModel.getSpiderSplitRules();

        //需要取值规则
        String rule = String.format("%s:%s",rulesModel.getSpiderRules(),rulesModel.getSerialNum());

        //需要格式化
        String formatting = rulesModel.getFormattingValue();

        //返回结果集
        List<Map<String, String>> kvList = JsonCommon.parsingJsonMap(repHtml,rule,splitRule,formatting,kvMap);//(html,

        String sql = CreatedSqlCommon.iniSaveSql(rulesModel.getTableName(), kvMap);

        //因为每天都会有时间，为了保证数据不重复，先删除
        generalService.delMapData(rulesModel.getTableName());

        //保存
        generalService.saveMapData(date,sql, kvList);

        spriderTaskService.updateTaskRulesEnd(id); //标识抓取结束
    }
}
