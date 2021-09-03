package com.yun.market.task;


import com.yun.market.task.spider.FoxxCodeTask;
import com.yun.market.task.spider.GeneralSpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/19 11:26
 */
@Component
public class DynamicSpiderTask extends ScheduledOfTask {

    @Autowired
    GeneralSpiderTask generalSpiderTask;

    @Autowired
    FoxxCodeTask foxxCodeTask;

    @Autowired
    GuBitDDxTask guBitDDxTask;


    public void execute() {

        generalSpiderTask.execute();

        foxxCodeTask.execute();

//        guBitDDxTask.execute();
    }
}
