package com.yun.market.service.hardanaly;

import com.yun.market.model.hardanaly.HardAnalyModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HardenAnalyService {
    void saveData(String sql, List<HardAnalyModel> list);
}
