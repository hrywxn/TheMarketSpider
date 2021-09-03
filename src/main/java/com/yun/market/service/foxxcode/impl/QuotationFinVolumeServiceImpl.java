package com.yun.market.service.foxxcode.impl;

import com.yun.market.model.quotation.QuotationFinVolumeModel;
import com.yun.market.service.foxxcode.QuotationFinVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationFinVolumeServiceImpl implements QuotationFinVolumeService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public void saveDataDo(List<QuotationFinVolumeModel> list) {
        createInboxIndex("foxxcode_index", QuotationFinVolumeModel.class);
        createInboxIndex("foxxcode_index","opendate_index",QuotationFinVolumeModel.class);
        mongoTemplate.insertAll(list);
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
