package com.yun.market.service.times.impl;

import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.model.spider.SpiderTaskRulesTimesModel;
import com.yun.market.service.times.TimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesServiceImpl implements TimesService {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer getRuleTimesByRulesId(int id) {

        String sql = String.format("SELECT task_rules_id FROM t_spider_task_rules_times where  task_rules_id = %s limit 1",id);

        Integer rulesId = null;
        try {
            rulesId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            return 0;
        }

        return  rulesId;
    }

    @Override
    public List<SpiderTaskRulesModel> getRulesModelList() {

        String sql = "SELECT * FROM t_spider_task_rules where status > 0";

        MapSqlParameterSource parame = new MapSqlParameterSource();

        List<SpiderTaskRulesModel> parameterMode = namedParameterJdbcTemplate.query(sql, parame, new BeanPropertyRowMapper(SpiderTaskRulesModel.class));

        return parameterMode;

    }

    @Override
    public void saveRulesTimes(String tableName, List<?> list) {

        String sql = CreatedSqlCommon.initCreatedTable(SpiderTaskRulesTimesModel.class, tableName);

        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));

    }

    @Override
    public SpiderTaskRulesTimesModel getAscOneDateTime() {

        String sql = "select * from t_spider_task_rules_times order by task_rules_times asc limit 1";

        MapSqlParameterSource parame = new MapSqlParameterSource();

        SpiderTaskRulesTimesModel rulesTimesModel = null;
        try {
            rulesTimesModel = namedParameterJdbcTemplate.queryForObject(sql, parame, new BeanPropertyRowMapper<SpiderTaskRulesTimesModel>(SpiderTaskRulesTimesModel.class));
        } catch (DataAccessException e) {
            return null;
        }

        return rulesTimesModel;
    }

    @Override
    public void updateRuleStatus(int id) {

        String sql = "update t_spider_task_rules set status = 10  where id = " + id;

        jdbcTemplate.execute(sql);

    }

    @Override
    public void delRuleTimesById(int id) {

        String sql = "delete from t_spider_task_rules_times where id = " + id;

        jdbcTemplate.execute(sql);

    }
}
