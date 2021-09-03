package com.yun.market.service.corn.impl;

import com.yun.market.model.corn.SpringScheduledCronModel;
import com.yun.market.service.corn.SpringScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/19 11:17
 */
@Service
@Transactional
public class SpringScheduledServiceImp implements SpringScheduledService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Cacheable(cacheNames = "cron")
    @Override
    public List<SpringScheduledCronModel> findAll() {

        String sql = String.format("SELECT `cron_id`, `cron_key`, `cron_expression`, `task_explain`, `status` FROM `t_spider_spring_scheduled_cron` LIMIT 0, 1000;");
        List<SpringScheduledCronModel> scheduledCronModelList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(SpringScheduledCronModel.class));
        return scheduledCronModelList;
    }

    @Cacheable(cacheNames = "cron", key = "#cronKey")
    @Override
    public SpringScheduledCronModel findByCronKey(String cronKey) {
        String sql = String.format("SELECT `cron_id`, `cron_key`, `cron_expression`, `task_explain`, `status` FROM `t_spider_spring_scheduled_cron` where cron_key='%s'", cronKey);
        List<SpringScheduledCronModel> scheduledCronModelList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(SpringScheduledCronModel.class));
        //结果判断
        if (scheduledCronModelList.size() > 0) {
            return scheduledCronModelList.get(scheduledCronModelList.size() - 1);
        } else {
            return new SpringScheduledCronModel();
        }
    }

    @Override
    public void updatePriorityStatus(int priority,int id) {
        String sql = String.format("UPDATE tk_scheduled_cron_inspection SET priority = %s WHERE id = %s",priority,id);
        jdbcTemplate.execute(sql);
    }

    @Override
    public List<String> getTenantList() {
        return null;
    }
}
