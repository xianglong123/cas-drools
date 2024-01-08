package com.cas.test;

import com.alibaba.fastjson.JSONObject;
import com.cas.DroolsApplication;
import com.cas.service.OrderDiscountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


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
@SpringBootTest(classes = DroolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleTest {

    @Autowired
    private OrderDiscountService orderDiscountService;


    @Test
    public void test3() {
        orderDiscountService.execute();
    }




}
