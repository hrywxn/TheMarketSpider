package com.yun.market.service.yye.impl;

import com.yun.market.model.shop.ShopModel;
import com.yun.market.model.yye.YybNodeModel;
import com.yun.market.service.yye.LhbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LhbServiceimpl implements LhbService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveData(String sql, List<YybNodeModel> list) {
        try {
            namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDataShop(String sql, List<ShopModel> list) {
        try {
            namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delData(String date) {
        String sql = String.format("delete from t_market_yyb where opendate >= %s",date);
        jdbcTemplate.execute(sql);
    }
}
