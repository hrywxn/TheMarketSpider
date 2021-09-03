package com.yun.market.service.yye;

import com.yun.market.model.shop.ShopModel;
import com.yun.market.model.yye.YybNodeModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LhbService {
    void saveData(String sql, List<YybNodeModel> list);

    void saveDataShop(String sql, List<ShopModel> list);

    void delData(String date);
}
