package com.yun.market.service.topten.impl;

import com.yun.market.model.topten.TopTurnoverModel;
import com.yun.market.service.topten.TopTurnoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TopTurnoverServiceImpl implements TopTurnoverService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveData(String sql, List<TopTurnoverModel> list) {
        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
    }
}
