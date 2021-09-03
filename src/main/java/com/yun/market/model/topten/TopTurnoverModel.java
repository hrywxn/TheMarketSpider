package com.yun.market.model.topten;

import lombok.Data;

@Data
public class TopTurnoverModel {
    private int rank;
    private String opendate="";
    private String foxxcode;
    private String name;

    private double pirce;
    private double ratio;

    private double hgtmrje;
    private double hgtmcje;
    private double hgtjme;
    private double hgtcjje;
    private String type;
}
