package com.yun.market.service.corn;

import com.yun.market.model.corn.SpringScheduledCronModel;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/19 10:35
 */
@Service
public interface SpringScheduledService {


    List<SpringScheduledCronModel> findAll();

    SpringScheduledCronModel findByCronKey(String cronKey);

//    List<SechduledCornInspecModel> findIsTaskEnable();
//
//    List<DbConfigModel> findDbIsEnable();

    void updatePriorityStatus(int priority,int id);

    List<String> getTenantList();
}
