package com.yun.market.model.push;

import lombok.Data;

/**
 * description: PushModel <br>
 * date: 2021/8/12 下午6:02 <br>
 * author: chenxiangfa <br>
 * version: 1.0 <br>
 */
@Data
public class PushModel {

    private String msgtype = "text";
    private Text text;

    public PushModel(Text text){
        this.text=text;
    }

}
