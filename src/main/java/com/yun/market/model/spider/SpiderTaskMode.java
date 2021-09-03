package com.yun.market.model.spider;

import lombok.Data;

@Data
public class SpiderTaskMode {
    private int id;
    private String spiderHttp;
    private String spiderHeader;
    private String fixOpendateHis;
    private int status;
    private String describe;
}