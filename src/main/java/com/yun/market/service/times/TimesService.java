package com.yun.market.service.times;

import com.yun.market.model.spider.SpiderTaskRulesModel;
import com.yun.market.model.spider.SpiderTaskRulesTimesModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimesService {

    Integer getRuleTimesByRulesId(int id);

    List<SpiderTaskRulesModel> getRulesModelList();

    void saveRulesTimes(String tableName, List<?> list);

    SpiderTaskRulesTimesModel getAscOneDateTime();

    void updateRuleStatus(int task_rules_id);

    void delRuleTimesById(int id);
}
