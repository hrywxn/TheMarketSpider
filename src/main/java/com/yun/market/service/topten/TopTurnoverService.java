package com.yun.market.service.topten;

import com.yun.market.model.topten.TopTurnoverModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopTurnoverService {
    void saveData(String sql, List<TopTurnoverModel> list);
}

