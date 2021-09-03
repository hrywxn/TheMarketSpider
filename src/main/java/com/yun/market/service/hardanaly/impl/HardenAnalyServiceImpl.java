package com.yun.market.service.hardanaly.impl;

import com.yun.market.model.hardanaly.HardAnalyModel;
import com.yun.market.service.hardanaly.HardenAnalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HardenAnalyServiceImpl implements HardenAnalyService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public void saveData(String sql, List<HardAnalyModel> list) {
        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
    }
}
