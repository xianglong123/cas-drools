package com.cas.test;

import cn.hutool.extra.spring.SpringUtil;
import com.cas.RuleApplication;
import com.cas.bo.RuleCacheBO;
import com.cas.bo.RuleGroup;
import com.cas.bo.RuleBaseBO;
import com.cas.constant.Constants;
import com.cas.service.RuleService;
import com.cas.service.impl.RuleOkServiceImpl;
import com.cas.util.RuleHelper;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.jeasy.rules.support.composite.ConditionalRuleGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xianglong
 * @create: 2023-12-26 21:25
 *
 * UnitRuleGroup：单元规则组是作为一个单元使用的组合规则，要么应用所有规则，要么不应用任何规则。
 * ActivationRuleGroup：激活规则组触发第一个适用规则并忽略组中的其他规则。规则首先按照其在组中的自然顺序(默认情况下优先级)进行排序。
 * ConditionalRuleGroup：条件规则组将具有最高优先级的规则作为条件，如果具有最高优先级的规则的计算结果为true，那么将触发其余的规则。
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleTest {

    @Resource
    private RuleCacheBO ruleCacheBO;

    @Resource
    private RuleOkServiceImpl ruleOkService;

    private RuleBaseBO getObject() {
        RuleBaseBO bo = new RuleBaseBO();
        bo.setActivityId("act0003");
        bo.setMobileNo("119");
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", "xianglong");
            put("age", "26");
        }};
        bo.setBehaviorData(map);
        return bo;
    }

    /**
     * 通过实例加载数据
     */
    @Test
    public void test2() {
        Facts facts = new Facts();
        facts.put(Constants.BASE_NAME, getObject());
        // define rules
        Rules rules = new Rules();
        groupRule(ruleCacheBO.getRg(), facts, rules);
        RuleHelper.generateRuleEngine().fire(rules, facts);
    }


    private void groupRule(List<RuleGroup> rgs, Facts facts, Rules rules) {

        rgs.forEach(x -> {
            ConditionalRuleGroup ruleGroup = new ConditionalRuleGroup(x.getId(), x.getGroupDesc());
            x.getRm().forEach(rm -> {
                facts.put(rm.getServiceAlias(), rm.getRuleService());
                ruleGroup.addRule(new MVELRule()
                        .name(rm.getName())
                        .priority(rm.getPriority())
                        .description(rm.getDescription())
                        .when(rm.getrCondition())
                        .then(rm.getServiceAlias() + "." + rm.getrAction()));
            });
            rules.register(ruleGroup);
        });
    }

    @Test
    public void test() {
        Map<String, RuleService> map = new HashMap<>();
        map.put("ruleSimpleServiceImpl", SpringUtil.getBean("ruleSimpleServiceImpl"));
        map.put("ruleSimple2ServiceImpl", SpringUtil.getBean("ruleSimple2ServiceImpl"));

        Rule rule2 = new MVELRule()
                .name("weather rule")
                .description("if it rains then take an umbrella")
                .when("rc.ok()")
//                .when("bo.behaviorData['name'] == 'xianglong'")
                .then("cu['ruleSimple2ServiceImpl'].execute(bo);");
        Facts facts = new Facts();
        facts.put(Constants.BASE_NAME, getObject());
        facts.put("cu", map);
        facts.put("rc", ruleOkService);
        // define rules
        Rules rules = new Rules();
        ConditionalRuleGroup ruleGroup = new ConditionalRuleGroup("aa", "2222");
        ruleGroup.addRule(rule2);

        rules.register(ruleGroup);
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
    }

    @Test
    public void testGroup() {
        Map<String, RuleService> map = new HashMap<>();
        map.put("ruleSimpleServiceImpl", SpringUtil.getBean("ruleSimpleServiceImpl"));
        map.put("ruleSimple2ServiceImpl", SpringUtil.getBean("ruleSimple2ServiceImpl"));


        Rule rule1 = new MVELRule()
                .name("weather rule")
                .description("if it rains then take an umbrella")
                .when("bo.mobileNo == '119'")
                .then("cu['ruleSimple2ServiceImpl'].execute(bo);");

        Rule rule2 = new MVELRule()
                .name("weather rule")
                .description("if it rains then take an umbrella")
                .when("bo.mobileNo == '119'")
                .then("cu['ruleSimpleServiceImpl'].execute(bo);");
        Facts facts = new Facts();
        facts.put(Constants.BASE_NAME, getObject());
        facts.put("cu", map);
        // define rules
        Rules rules = new Rules();
        ConditionalRuleGroup ruleGroup = new ConditionalRuleGroup("0001", "2222");
        ruleGroup.addRule(rule1);

        ConditionalRuleGroup ruleGroup2 = new ConditionalRuleGroup("0002", "22223");
        ruleGroup2.addRule(rule2);

        rules.register(ruleGroup);
        rules.register(ruleGroup2);
        RulesEngine rulesEngine = RuleHelper.generateRuleEngine();
        rulesEngine.fire(rules, facts);
    }




}
