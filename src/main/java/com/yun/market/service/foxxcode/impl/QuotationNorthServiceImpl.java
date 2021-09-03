package com.yun.market.service.foxxcode.impl;

import com.yun.market.model.quotation.QuotationNorthModel;
import com.yun.market.service.foxxcode.QuotationNorthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationNorthServiceImpl implements QuotationNorthService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveData(String sql,List<QuotationNorthModel> list) {
        String sqlDel = "delete from t_market_quotation_north_time";
        jdbcTemplate.execute(sqlDel);
        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
    }
}
