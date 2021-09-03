package com.yun.market.service.sprider;

import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.model.spider.SpiderTaskRulesModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpriderTaskService {

    SpiderTaskMode getSpiderTaskMode();

    SpiderTaskRulesModel getSpiderTaskRulesModel();

    SpiderTaskMode getSpiderTaskMode(int taskId);

    List<SpiderTaskParameterMode> getSpiderTaskParaMode(int taskId);

    List<SpiderTaskRulesModel> getSpiderTaskRulesMode(int taskId);

    void updateTask(String id,int status);

    void updateTaskStart(SpiderTaskMode spiderNode, String taskName);

    void updateTaskEnd(SpiderTaskMode spiderNode, String taskName);

    void updateTaskRulesStart(Integer id);

    void updateTaskRulesEnd(Integer id);
}
