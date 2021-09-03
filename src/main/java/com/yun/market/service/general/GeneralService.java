package com.yun.market.service.general;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GeneralService {

    void saveData(String tableName,List<?> list);

    void saveMapData(String date, String tableName, List<Map<String, String>> kvList);

    void delMapData(String tableName);
}
