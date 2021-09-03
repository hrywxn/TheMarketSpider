package com.yun.market.service.foxxcode.impl;


import com.yun.market.model.quotation.QuotationDayModel;
import com.yun.market.service.foxxcode.QuotationDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationDayServiceImpl implements QuotationDayService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveQuotationDay(List<QuotationDayModel> list) {
        //先创建索引
        createInboxIndex("foxxcode_index",QuotationDayModel.class);
        createInboxIndex("foxxcode_index","opendate_index",QuotationDayModel.class);
        mongoTemplate.insertAll(list);
    }

    @Override
    public void delQuotationDay(String foxxcode) {
        Query query = new Query(Criteria.where("foxxcode").is(foxxcode));

        mongoTemplate.remove(query,QuotationDayModel.class);
    }



    /**
     * 创建联合索引
     *
     * @param index_key 索引的名称
     * @param index_key2 索引的名称
     * @param collectionName 集合名称
     * @return
     */
    public boolean createInboxIndex(String index_key, String index_key2,  Class<?>collectionName) {
        boolean scuess = true;
        try {
            Index index = new Index();
            index.on(index_key, Sort.Direction.ASC).on(index_key2, Sort.Direction.ASC);
            mongoTemplate.indexOps(collectionName).ensureIndex(index);

        } catch (Exception ex) {
            scuess = false;
        }
        return scuess;
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
