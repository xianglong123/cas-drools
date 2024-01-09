package com.cas.controller;

import com.cas.bo.DroolsRule;
import com.cas.config.DroolsDrlManager;
import com.cas.service.DroolsRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
@RestController
@RequestMapping("/drools/rule")
public class DrlController {

    @Resource
    private DroolsRuleService droolsRuleService;
    @Resource
    private DroolsDrlManager droolsManager;

    @GetMapping("findAll")
    public List<DroolsRule> findAll() {
        return droolsRuleService.findAll();
    }

    @PostMapping("add")
    public String addRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.addDroolsRule(droolsRule);
        return "添加成功";
    }

    @PostMapping("update")
    public String updateRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.updateDroolsRule(droolsRule);
        return "修改成功";
    }

    @PostMapping("deleteRule")
    public String deleteRule(Long ruleId, String ruleName) {
        droolsRuleService.deleteDroolsRule(ruleId, ruleName);
        return "删除成功";
    }

    @GetMapping("fireRule")
    public String fireRule(String kieBaseName, Integer param) {
        return droolsManager.fireRule(kieBaseName, param);
    }
}
