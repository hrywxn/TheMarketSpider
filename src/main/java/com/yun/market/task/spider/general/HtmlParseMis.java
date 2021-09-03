package com.yun.market.task.spider.general;

import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.common.HtmlParCommon;
import com.yun.market.common.ParsingHttp;
import com.yun.market.common.RulesCommon;
import com.yun.market.config.HttpConfig;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.service.general.GeneralService;
import com.yun.market.service.sprider.SpriderTaskService;
import com.yun.market.task.spider.GeneralSpiderTask;
import com.yun.market.util.TimesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class HtmlParseMis extends GeneralSpiderTask {
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

        // 规则
        String rules = String.format("%s->%s", rulesModel.getSpiderRules(), rulesModel.getSerialNum());

        //格式化
        String formatting = rulesModel.getFormattingValue();

        //查看规则中是否需要分多次爬取的关键字 getMax,默认getMaX=20
        //初始化映射列名
        Map<String, String> kvMap = RulesCommon.iniKeyValueMap(rulesModel.getMappingColumn());

        //解析Html，返回结果集
        List<Map<String, String>> kvList = HtmlParCommon.doParsinHtmlToContent(html,formatting,rules, kvMap);

        String sql = CreatedSqlCommon.iniSaveSql(rulesModel.getTableName(), kvMap);

        //因为每天都会有时间，为了保证数据不重复，先删除
//        generalService.delMapData(rulesModel.getTableName());

        String date = TimesUtil.DateToStr(new Date());

        //保存
        generalService.saveMapData(date, sql, kvList);

        spriderTaskService.updateTaskRulesEnd(id); //标识抓取结束
    }
}
