package com.yun.market.service.general.impl;

import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.model.hardanaly.HardAnalyModel;
import com.yun.market.service.general.GeneralService;
import com.yun.market.util.TimesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveData(String tableName, List<?> list) {
        String sql = CreatedSqlCommon.initCreatedTable(HardAnalyModel.class, tableName);
        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
    }

    @Override
    public void saveMapData(String date, String sql, List<Map<String, String>> kvList) {

        for (Map paramMap : kvList) {

            try {

                paramMap.put("opendate", date.replace("-",""));

                namedParameterJdbcTemplate.update(sql,  paramMap );

            } catch (DataAccessException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void delMapData(String tableName) {
        String sql = String.format("delete from %s where opendate = %s",tableName,TimesUtil.DateToStr(new Date()));
        jdbcTemplate.execute(sql);
    }
}
