package com.yun.market.model.yye;

import lombok.Data;

@Data
public class YybNodeModel {
    private String opendate;
    private String foxxcode;
    private String name;
    private double price;
    private double ratio;
    private String com;
    private String comcode="";
    private String type;
    private double  buyamount;
    private double  sellamount;
    private double  netamount;
    private double  allamount;
    private double  increase;
}
