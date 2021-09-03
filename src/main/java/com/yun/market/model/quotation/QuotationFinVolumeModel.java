package com.yun.market.model.quotation;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "t_market_quotation_finvolume")
public class QuotationFinVolumeModel {
    private int opendate;
    private String foxxcode;
    private String name;
    private long rzye;
    private double close;
}
