package com.yun.market.model.spider;

import lombok.Data;

@Data
public class SpiderTaskRulesModel{
    private int id =0;
    private int taskId;
    private String spiderRules;
    private String spiderSplitRules;
    private String spiderTime;
    private String serialNum;
    private String mappingColumn;
    private String describe;
    private String contentType;
    private String tableName;
    private String formattingValue;
    private String splitValue;
    private int status;
}
