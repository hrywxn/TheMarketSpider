package com.yun.market.task;

import com.yun.market.model.corn.SpringScheduledCronModel;
import com.yun.market.service.corn.SpringScheduledService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/20 11:05
 */
public class ScheduledOfTask implements Runnable{

    @Autowired
    SpringScheduledService springScheduledService;

    /**
     * 定时任务方法
     */
    public void execute() {
    }

    /**
     * 实现控制定时任务启用或禁用的功能
     */
    @Override
    public void run() {

        SpringScheduledCronModel scheduledCron = springScheduledService.findByCronKey(this.getClass().getName());

        if (scheduledCron.getStatus()==2) {
            // 任务是禁用状态
            return;
        }

        execute();
    }
}


