package com.yun.market.model.ddx;

import lombok.Data;

/**
 * description: GuBitDDxMode <br>
 * date: 2021/8/24 下午4:42 <br>
 * author: chenxiangfa <br>
 * version: 1.0 <br>
 */
@Data
public class GuBitDDxMode {

    private String opendate;
    private String code;
    private double newPrice;
    private double ratio;
    private double  allamount;
    private double singleNum;

}
