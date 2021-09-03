package com.yun.market.task.times;

import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.model.spider.SpiderTaskRulesTimesModel;
import com.yun.market.service.times.TimesService;
import com.yun.market.util.TimesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TimesGenerator {

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

        //获取规则列表有效的所有任务
        List<SpiderTaskRulesModel> rulesModelList = timesService.getRulesModelList();

        //查看时间列表中是否含有
        for (SpiderTaskRulesModel rule : rulesModelList) {

            int rulesId = timesService.getRuleTimesByRulesId(rule.getId());

            //如果没有，则说明还未生成时间
            if (rulesId > 0) {
                continue;
            }

            //生成规则任务时间列表
            doTimesGenerator(rule);
        }

    }

    /**
     * 生成规则任务时间列表
     * @param rule
     */
    private void doTimesGenerator(SpiderTaskRulesModel rule) {

        //调用时间生成器
        List<Date> dateList = TimesUtil.getCronToDate(rule.getSpiderTime());

        if (dateList.size() < 1) {
            return;
        }

        List<SpiderTaskRulesTimesModel> rulesTimesModelList = new ArrayList<>();

        for (Date date : dateList) {

            SpiderTaskRulesTimesModel ruleTimesModel = new SpiderTaskRulesTimesModel();

            ruleTimesModel.setTask_rules_id(rule.getId());

            ruleTimesModel.setTask_rules_times(date);

            rulesTimesModelList.add(ruleTimesModel);
        }

        //定义保存的表
        String tableName = "t_spider_task_rules_times";

        timesService.saveRulesTimes(tableName, rulesTimesModelList);

        logger.info(String.format("========= %s ==========","生成爬虫规则时间表数据,触发规则表生成的时间Id为:"+rule.getId()));
    }

}
