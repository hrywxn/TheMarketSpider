package com.yun.market.service.push.impl;

import com.yun.market.model.push.YybModel;
import com.yun.market.service.push.PushService;
import com.yun.market.util.TimesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * description: PushServiceImpl <br>
 * date: 2021/8/12 下午5:39 <br>
 * author: chenxiangfa <br>
 * version: 1.0 <br>
 */
@Service
public class PushServiceImpl implements PushService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<YybModel> getByLocalTimeData() {

        String sql =String.format("SELECT\n" +
                "\tsum(buyamount) AS buyamount,\n" +
                "\tSUM(sellamount) AS sellamount,\n" +
                "\tSUM(buyamount - sellamount) AS jmeamount,\n" +
                "\tGROUP_CONCAT(DISTINCT(`foxxcode`)) AS foxxcode,\n" +
                "\tGROUP_CONCAT(DISTINCT(`name`)) AS NAMES,\n" +
                "\tcom,\n" +
                "\topendate\n" +
                "FROM\n" +
                "\tt_market_yyb\n" +
                "WHERE\n" +
                "\topendate = '%s'\n" +
                "GROUP BY\n" +
                "\tcom\n" +
                "ORDER BY\n" +
                "\tjmeamount DESC\n" +
                "LIMIT 10", TimesUtil.DateToStr(new Date()));
        List<YybModel> yybModels = jdbcTemplate.query(sql, new BeanPropertyRowMapper(YybModel.class));
        return yybModels;
    }
}
