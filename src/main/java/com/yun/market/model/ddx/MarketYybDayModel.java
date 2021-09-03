package com.yun.market.model.ddx;

import lombok.Data;

@Data
public class MarketYybDayModel {
    private String opendate;
    private String foxxcode;
    private double  price;
    private double  odds;
}
