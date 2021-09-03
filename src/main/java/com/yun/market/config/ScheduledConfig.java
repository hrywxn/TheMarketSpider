package com.yun.market.config;

import com.yun.market.model.corn.SpringScheduledCronModel;
import com.yun.market.service.corn.SpringScheduledService;
import com.yun.market.task.ScheduledOfTask;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/19 10:32
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SpringScheduledService springScheduledService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        List<SpringScheduledCronModel> list = springScheduledService.findAll();

        for (SpringScheduledCronModel springScheduledCron : list) {

            Class<?> clazz;

            Object task;

            try {

                clazz = Class.forName(springScheduledCron.getCronKey());

                task = context.getBean(clazz);

            } catch (ClassNotFoundException e) {

                throw new IllegalArgumentException("spring_scheduled_cron表数据" + springScheduledCron.getCronKey() + "有误", e);

            } catch (BeansException e) {

                throw new IllegalArgumentException(springScheduledCron.getCronKey() + "未纳入到spring管理", e);

            }

            Assert.isAssignable(ScheduledOfTask.class, task.getClass(), "定时任务类必须集成ScheduledOfTask");

            taskRegistrar.addTriggerTask(((Runnable) task),

                    triggerContext -> {

                        String cronExpression = springScheduledService.findByCronKey(springScheduledCron.getCronKey()).getCronExpression();

                        //计划任务表达式为空则跳过
                        return new CronTrigger(cronExpression).nextExecutionTime(triggerContext);

                    }
            );
        }

    }

    @Bean
    public Executor taskExecutor() {

        return Executors.newScheduledThreadPool(10);

    }
}

