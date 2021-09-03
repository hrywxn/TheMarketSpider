package com.yun.market.model.shop;

import lombok.Data;

@Data
public class ShopModel {
    private String name;
    private String ctime;
    private String shop_location;
    private String sold;
    private String view_count;
    private String type;
    private double price;
}
