package com.yun.market.model.spider;

import lombok.Data;

import java.util.Date;

@Data
public class SpiderTaskRulesTimesModel {
    private int id =0;
    private int task_rules_id=0;
    private Date task_rules_times;
}
