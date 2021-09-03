package com.yun.market.model.corn;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author jiangwc
 * @version 创建时间：2021/1/19 10:38
 */
@Data
@Component
public class SpringScheduledCronModel {
    private Integer cronId;
    private String cronKey;
    private String cronExpression;
    private String taskExplain;
    private Integer status;
}
