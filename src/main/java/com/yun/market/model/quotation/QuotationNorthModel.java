package com.yun.market.model.quotation;

import lombok.Data;

@Data
public class QuotationNorthModel {
    private int opendate;
    private String time="";
    private double hgjme;
    private double sgjme;
    private double bsjme;

    private double hgmre;
    private double hgmce;
    private double sgmre;
    private double sgmce;
    private double bsmre;
    private double bsmce;
}
