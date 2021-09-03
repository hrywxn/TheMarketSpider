package com.yun.market.task.times;

import com.yun.market.model.spider.SpiderTaskRulesTimesModel;
import com.yun.market.service.times.TimesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimesChecker {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TimesService timesService;

    public void execute() {
        try {
            doStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doStart() {

        //获取最新一条时间生成器的时间
        SpiderTaskRulesTimesModel rulesTimesModel = timesService.getAscOneDateTime();

        if (rulesTimesModel==null){
            return;
        }

        //对比当前时间和查询时间,小于则返回，大于则修改
        Date dateNow = new Date();

        Date queryDate = rulesTimesModel.getTask_rules_times();

        if (dateNow.before(queryDate)){
            return;
        }

        logger.info(String.format("========= %s ==========","更新爬虫规则表数据状态,当前状态Id为:"+rulesTimesModel.getTask_rules_id()));

        //更改规则表
        timesService.updateRuleStatus(rulesTimesModel.getTask_rules_id());

        //删除当前Id
        timesService.delRuleTimesById(rulesTimesModel.getId());

        logger.info(String.format("========= %s ==========","更新爬虫规则时间表数据,删除的Id为:"+rulesTimesModel.getId()));

    }

}
