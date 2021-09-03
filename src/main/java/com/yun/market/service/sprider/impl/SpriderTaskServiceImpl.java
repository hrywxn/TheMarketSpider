package com.yun.market.service.sprider.impl;

import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.service.sprider.SpriderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpriderTaskServiceImpl implements SpriderTaskService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public SpiderTaskMode getSpiderTaskMode() {
        String sql = "SELECT * FROM t_spider_task where status =10 LIMIT 1";
        MapSqlParameterSource parame = new MapSqlParameterSource();
        SpiderTaskMode spiderTaskMode = null;
        try {
            spiderTaskMode = namedParameterJdbcTemplate.queryForObject(sql, parame, new BeanPropertyRowMapper<SpiderTaskMode>(SpiderTaskMode.class));
        } catch (DataAccessException e) {
            return spiderTaskMode;
        }
        return spiderTaskMode;
    }

    @Override
    public SpiderTaskRulesModel getSpiderTaskRulesModel() {
        String sql = "SELECT * FROM t_spider_task_rules where status =10 LIMIT 1";
        MapSqlParameterSource parame = new MapSqlParameterSource();
        SpiderTaskRulesModel dataList = null;
        try {
            dataList = namedParameterJdbcTemplate.queryForObject(sql, parame, new BeanPropertyRowMapper<SpiderTaskRulesModel>(SpiderTaskRulesModel.class));
        } catch (DataAccessException e) {
            return dataList;
        }
        return dataList;
    }

    @Override
    public SpiderTaskMode getSpiderTaskMode(int id) {
        String sql = "SELECT * FROM t_spider_task where  id =" + id;
        MapSqlParameterSource parame = new MapSqlParameterSource();
        SpiderTaskMode parameterMode = namedParameterJdbcTemplate.queryForObject(sql, parame, new BeanPropertyRowMapper<SpiderTaskMode>(SpiderTaskMode.class));
        return parameterMode;
    }

    @Override
    public List<SpiderTaskParameterMode> getSpiderTaskParaMode(int taskId) {
        String sql = "SELECT * FROM t_spider_task_parameter where spider_task_id =" + taskId;
        MapSqlParameterSource parame = new MapSqlParameterSource();
        List<SpiderTaskParameterMode> parameterMode = namedParameterJdbcTemplate.query(sql, parame, new BeanPropertyRowMapper(SpiderTaskParameterMode.class));
        return parameterMode;
    }

    @Override
    public List<SpiderTaskRulesModel> getSpiderTaskRulesMode(int taskId) {
        String sql = "SELECT * FROM t_spider_task_rules where task_id =" + taskId + " limit 1";
        MapSqlParameterSource parame = new MapSqlParameterSource();
        List<SpiderTaskRulesModel> parameterMode = namedParameterJdbcTemplate.query(sql, parame, new BeanPropertyRowMapper(SpiderTaskRulesModel.class));
        return parameterMode;
    }

    @Override
    public void updateTask(String id, int status) {
        String sql = "UPDATE t_spider_task SET status =" + status + " where id =" + id;
        MapSqlParameterSource parame = new MapSqlParameterSource();
        namedParameterJdbcTemplate.update(sql, parame);
    }

    @Override
    public void updateTaskStart(SpiderTaskMode spiderNode, String taskName) {
        if (!spiderNode.getDescribe().contains(taskName)) {
            return;
        }
        String id = String.valueOf(spiderNode.getId());

        String sql = "UPDATE t_spider_task SET status =" + 20 + " where id =" + id;
        MapSqlParameterSource parame = new MapSqlParameterSource();
        namedParameterJdbcTemplate.update(sql, parame);
    }

    @Override
    public void updateTaskEnd(SpiderTaskMode spiderNode, String taskName) {
        if (!spiderNode.getDescribe().contains(taskName)) {
            return;
        }
        String id = String.valueOf(spiderNode.getId());

        String sql = "UPDATE t_spider_task SET status =" + 80 + " where id =" + id;
        MapSqlParameterSource parame = new MapSqlParameterSource();
        namedParameterJdbcTemplate.update(sql, parame);
    }

    @Override
    public void updateTaskRulesStart(Integer id) {
        String sql = "UPDATE t_spider_task_rules SET status =" + 20 + " where id =" + id;
        MapSqlParameterSource parame = new MapSqlParameterSource();
        namedParameterJdbcTemplate.update(sql, parame);
    }

    @Override
    public void updateTaskRulesEnd(Integer id) {
        String sql = "UPDATE t_spider_task_rules SET status =" + 80 + " where id =" + id;
        MapSqlParameterSource parame = new MapSqlParameterSource();

        namedParameterJdbcTemplate.update(sql, parame);
    }
}
