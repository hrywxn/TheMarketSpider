package com.yun.market.service.foxxcode.impl;

import com.yun.market.model.FoxxcodeMode;
import com.yun.market.service.foxxcode.FoxxcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoxxcodeServiceImpl implements FoxxcodeService {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveFoxxcode(String sql,List<FoxxcodeMode> list) {
        createInboxIndex("foxxcode_index",FoxxcodeMode.class);
        mongoTemplate.insertAll(list);
//        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
    }

    @Override
    public List<FoxxcodeMode> getFoxxcodeList() {
        String sql = "SELECT FOXXCODE FROM t_market_foxxcode";
        List<FoxxcodeMode> foxxcodeModeList = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(FoxxcodeMode.class));
        return foxxcodeModeList;
    }

    @Override
    public void updateFoxxcodeStatus(String foxxcode) {
        String sql =String.format("update t_market_foxxcode set status = 80 where foxxcode in (%s)",foxxcode);
        MapSqlParameterSource parame = new MapSqlParameterSource();
        namedParameterJdbcTemplate.update(sql,parame);
    }

    /**
     * 创建单个索引
     *
     * @param index_key 索引的名称
     * @param collectionName 集合名称
     * @return
     */
    public boolean createInboxIndex(String index_key, Class<?>collectionName) {
        boolean scuess = true;
        try {
            Index index = new Index();
            index.on(index_key, Sort.Direction.ASC);
            mongoTemplate.indexOps(collectionName).ensureIndex(index);

        } catch (Exception ex) {
            scuess = false;
        }
        return scuess;
    }
}
