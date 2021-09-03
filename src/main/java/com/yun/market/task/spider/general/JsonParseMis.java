package com.yun.market.task.spider.general;

import com.yun.market.common.CreatedSqlCommon;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JsonParseMis {
    @Autowired
    SpriderTaskService spriderTaskService;
    @Autowired
    GeneralService generalService;

    public void doStartGeneMis(SpiderTaskRulesModel rulesModel, SpiderTaskMode spiderNode) {

        int id = rulesModel.getId();
        spriderTaskService.updateTaskRulesStart(id); //标识抓取开始

        //标准化请求头
        List<SpiderTaskParameterMode> parameterModeList = ParsingHttp.initHttpHeader(spiderNode);

        //获取内容
        String html = HttpConfig.doGet(spiderNode.getSpiderHttp(), parameterModeList);

        String repHtml = RulesCommon.displaceServcie(html, rulesModel.getSplitValue());

        Map<String, String> kvMap = RulesCommon.iniKeyValueMap(rulesModel.getMappingColumn());

        List<Map<String, String>> kvList = ParsingHttp.parsingMap(repHtml, rulesModel.getSpiderRules());

        String sql = CreatedSqlCommon.iniSaveSql(rulesModel.getTableName(), kvMap);

        String date = TimesUtil.DateToStr(new Date());

        //因为每天都会有时间，为了保证数据不重复，先删除
        generalService.delMapData(rulesModel.getTableName());

        //保存
        generalService.saveMapData(date, sql, kvList);

        spriderTaskService.updateTaskRulesEnd(id); //标识抓取结束
    }
}
