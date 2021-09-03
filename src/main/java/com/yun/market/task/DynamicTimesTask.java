package com.yun.market.task;


import com.yun.market.task.times.TimesChecker;
import com.yun.market.task.times.TimesGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/19 11:26
 */
@Component
public class DynamicTimesTask extends ScheduledOfTask {

    @Autowired
    TimesChecker timesChecker;

    @Autowired
    TimesGenerator timesGenerator;

    public void execute() {

        timesGenerator.execute(); //时间生成器 *根据erp后台输入的时间生成单任务一天的所有时间

        timesChecker.execute(); //时间检查器 *检查是否到时间更改任务列表的状态

    }
}
