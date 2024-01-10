package com.cas.service;

import com.cas.bo.DroolsRule;
import com.cas.config.DroolsDrlManager;
import com.cas.config.DroolsJarManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
@Service
public class DroolsRuleServiceImpl implements DroolsRuleService {

    @Resource
    private DroolsDrlManager droolsManager;
    @Resource
    private DroolsJarManager droolsJarManager;

    /**
     * 模拟数据库
     */
    private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);

    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleMap.values());
    }

    @Override
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        if (droolsRule.getType().equals("drl")) {
            droolsManager.addOrUpdateRule(droolsRule);
        } else {
            droolsJarManager.addOrUpdateRule(droolsRule);
        }
    }

    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleMap.get(ruleId);
        if (null != droolsRule) {
            droolsRuleMap.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule, ruleName);
        }
    }
}
