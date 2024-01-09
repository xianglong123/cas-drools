package com.cas.config;

import com.cas.bo.DroolsRule;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
@Configuration
public class DroolsJarManager {

    private static final Logger log = LoggerFactory.getLogger(DroolsJarManager.class);

    // 此类本身就是单例的
    private final KieServices kieServices = KieServices.get();
    private final KieRepository repository = kieServices.getRepository();
    // 需要全局唯一一个，如果每次加个规则都新创建一个，那么旧需要销毁之前创建的kieContainer，如果此时有正在使用的KieSession，则可能有问题
    private KieContainer kieContainer;
//    private static final String url = "http://localhost:8080/business-central/maven2/com/cas/cRule/1.0.0-SNAPSHOT/";
//    private static final String url = "/Users/xianglong/.m2/repository/com/cas/cas-drools-kjar/1.0-SNAPSHOT/cas-drools-kjar-1.0-20240109.023344-2.jar";
    private static final String url = "/Users/xianglong/Downloads/cRule-1.0.0-20240105.064124-3.jar";


    @PreDestroy
    public void destroy() {
        if (null != kieContainer) {
            kieContainer.dispose();
        }
    }

    @PostConstruct
    public void setUp() {
        log.info("222222222222");
        KieServices ks = KieServices.Factory.get();

        ReleaseId releaseId = ks.newReleaseId("com.cas", "cas-drools-kjar", "1.0.0");

        kieContainer = ks.newKieContainer(releaseId);
//        KieScanner kScanner = ks.newKieScanner(kieContainer);

        // Start the KieScanner polling the Maven repository every 10 seconds
//        kScanner.start(10000L);
    }


    /**
     * 添加或更新 drools 规则
     */
    public void addOrUpdateRule(DroolsRule droolsRule) {
        try {
            FileInputStream fis = new FileInputStream(url);
            KieModule kieModule = repository.addKieModule(kieServices.getResources().newInputStreamResource(fis));
            if (null == kieContainer) {
                kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
//                KieScanner kScanner = kieServices.newKieScanner(kieContainer);
//                // 100秒刷新一次
//                kScanner.start(10L);
//                kScanner.addListener(new KieScennerListener());
            } else {
                // 实现动态更新
                ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieModule);
            }

        } catch (Exception e) {
            log.error("", e);
        }
    }

    public KieContainer getKieContainer() {
        return kieContainer;
    }
}

//    /**
//     * 添加或更新 drools 规则
//     */
//    public void addOrUpdateRule(DroolsRule droolsRule) {
//        UrlResource resource = (UrlResource) kieServices.getResources().newUrlResource(url + droolsRule.getJarName());
//        resource.setUsername("admin");
//        resource.setPassword("admin");
//        resource.setBasicAuthentication("enabled");
//        try {
//            KieModule kieModule = repository.addKieModule(kieServices.getResources().newInputStreamResource(resource.getInputStream()));
//            if (null == kieContainer) {
//                kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
//                KieScanner kScanner = kieServices.newKieScanner(kieContainer);
//                // 100秒刷新一次
//                kScanner.start(10L);
//                kScanner.addListener(new KieScennerListener());
//            } else {
//                // 实现动态更新
//                ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieModule);
//            }
//
//        } catch (Exception e) {
//        }
//    }
//
//    public KieContainer getKieContainer() {
//        return kieContainer;
//    }
//}
