package com.yun.market.service.push;

import com.yun.market.model.push.YybModel;

import java.util.List;

/**
 * description: PushService <br>
 * date: 2021/8/12 下午5:39 <br>
 * author: chenxiangfa <br>
 * version: 1.0 <br>
 */
public interface PushService {

    List<YybModel> getByLocalTimeData();

}
