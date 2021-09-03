package com.yun.market.model.spider;

import lombok.Data;

@Data
public class SpiderTaskParameterMode {
    private String spiderTaskId;
    private String rawKey;
    private String rawValue;

    public SpiderTaskParameterMode(String rawKey, String rawValue) {
        this.rawKey = rawKey;
        this.rawValue = rawValue;
    }
    public SpiderTaskParameterMode() {
    }
}
