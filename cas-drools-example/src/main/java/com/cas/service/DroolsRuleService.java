package com.cas.service;


import com.cas.bo.DroolsRule;

import java.util.List;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
public interface DroolsRuleService {
    /**
     * 从数据库中加载所有的drools规则
     */
    List<DroolsRule> findAll();

    /**
     * 添加drools规则
     */
    void addDroolsRule(DroolsRule droolsRule);

    /**
     * 修改drools 规则
     */
    void updateDroolsRule(DroolsRule droolsRule);

    /**
     * 删除drools规则
     */
    void deleteDroolsRule(Long ruleId, String ruleName);
}
