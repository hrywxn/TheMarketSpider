//package com.yun.market.common;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.index.Index;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MongoCreateIndexCom {
//
//    @Autowired
//    static
//    MongoTemplate mongoTemplate;
//
//    /**
//     * 创建联合索引
//     *
//     * @param index_key 索引的名称
//     * @param index_key2 索引的名称
//     * @param collectionName 集合名称
//     * @return
//     */
//    public static boolean createInboxIndex(String index_key, String index_key2,  Class<?>collectionName) {
//        boolean scuess = true;
//        try {
//            Index index = new Index();
//            index.on(index_key, Sort.Direction.ASC).on(index_key2, Sort.Direction.ASC);
//            mongoTemplate.indexOps(collectionName).ensureIndex(index);
//
//        } catch (Exception ex) {
//            scuess = false;
//        }
//        return scuess;
//    }
//
//
//    /**
//     * 创建单个索引
//     *
//     * @param index_key 索引的名称
//     * @param collectionName 集合名称
//     * @return
//     */
//    public static boolean createInboxIndex(String index_key, Class<?>collectionName) {
//        boolean scuess = true;
//        try {
//            Index index = new Index();
//            index.on(index_key, Sort.Direction.ASC);
//            mongoTemplate.indexOps(collectionName).ensureIndex(index);
//
//        } catch (Exception ex) {
//            scuess = false;
//        }
//        return scuess;
//    }
//}
