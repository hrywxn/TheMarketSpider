package com.yun.market.model.quotation;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "t_market_quotation_day")
public class QuotationDayModel {
    private static final long serialVersionUID = -3258839839160856613L;
    private String foxxcode;
    private int opendate;
    private String name="";
    private double open;
    private double high;
    private double low;
    private double close;
    private double pre_close;
    private String rise_amount;
    private String rise_applies;
    private String vol;
    private String amout;
    private String lt_value;
    private String sz_value;
}
