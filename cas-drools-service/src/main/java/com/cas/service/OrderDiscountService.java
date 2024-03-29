package com.cas.service;

import com.cas.config.DroolsJarManager;
import com.cas.crule.Product;
import com.cas.crule.User;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
@Service
public class OrderDiscountService {

    @Autowired
    private DroolsJarManager droolsJarManager;
    @Resource
    private PrintService printService;

    public void execute() {
        KieSession kSession = droolsJarManager.getKieContainer().newKieSession();
        kSession.insert(new User("Dave",100));
        kSession.fireAllRules();
        kSession.dispose();
    }

    public void pay() {
        KieSession kSession = droolsJarManager.getKieContainer().newKieSession();
        kSession.insert(new Product("Dave",100));
        kSession.setGlobal("printService", printService);
        kSession.fireAllRules();
        kSession.dispose();
    }
}
