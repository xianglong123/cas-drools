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
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
@Component
public class DroolsJarManager {

    private static final Logger log = LoggerFactory.getLogger(DroolsJarManager.class);

    // 此类本身就是单例的
    private final KieServices kieServices = KieServices.get();
    // kie文件系统，需要缓存，如果每次添加规则都是重新new一个的话，则可能出现问题。即之前加到文件系统中的规则没有了
    private final KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    private final KieRepository repository = kieServices.getRepository();
    // 可以理解为构建 kmodule.xml
    private final KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    // 需要全局唯一一个，如果每次加个规则都新创建一个，那么旧需要销毁之前创建的kieContainer，如果此时有正在使用的KieSession，则可能有问题
    private KieContainer kieContainer;
    private static final String url = "http://localhost:8080/business-central/maven2/com/cas/cRule/1.0.0-SNAPSHOT/";


    @PreDestroy
    public void destroy() {
        if (null != kieContainer) {
            kieContainer.dispose();
        }
    }

    /**
     * 判断该kbase是否存在
     */
    public boolean existsKieBase(String kieBaseName) {
        if (null == kieContainer) {
            return false;
        }
        Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
        if (kieBaseNames.contains(kieBaseName)) {
            return true;
        }
        log.info("需要创建KieBase:{}", kieBaseName);
        return false;
    }

    public void deleteDroolsRule(DroolsRule droolsRule, String ruleName) {
        String kieBaseName = droolsRule.getKieBaseName();
        String packageName = droolsRule.getKiePackageName();
        if (existsKieBase(kieBaseName)) {
            KieBase kieBase = kieContainer.getKieBase(kieBaseName);
            kieBase.removeRule(packageName, ruleName);
            String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
            kieFileSystem.delete(file);
            buildKieContainer();
            log.info("删除kieBase:[{}]包:[{}]下的规则:[{}]", kieBaseName, packageName, ruleName);
        }
    }

    /**
     * 添加或更新 drools 规则
     */
    public void addOrUpdateRule(DroolsRule droolsRule) {
        UrlResource resource = (UrlResource) kieServices.getResources().newUrlResource(url + droolsRule.getJarName());
        resource.setUsername("admin");
        resource.setPassword("admin");
        resource.setBasicAuthentication("enabled");
        try {
            KieModule kieModule = repository.addKieModule(kieServices.getResources().newInputStreamResource(resource.getInputStream()));
            if (null == kieContainer) {
                kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
            } else {
                // 实现动态更新
                ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieModule);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 构建KieContainer
     */
    private void buildKieContainer() {
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        // 通过KieBuilder构建KieModule下所有的KieBase
        kieBuilder.buildAll();
        // 获取构建过程中的结果
        Results results = kieBuilder.getResults();
        // 获取错误信息
        List<Message> messages = results.getMessages(Message.Level.ERROR);
        if (null != messages && !messages.isEmpty()) {
            for (Message message : messages) {
                log.error(message.getText());
            }
            throw new RuntimeException("加载规则出现异常");
        }
        // KieContainer只有第一次时才需要创建，之后就是使用这个
        if (null == kieContainer) {
            kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        } else {
            // 实现动态更新
            ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
        }
    }

    /**
     * 触发规则，此处简单模拟，会向规则中插入一个Integer类型的值
     */
    public String fireRule(String kieBaseName, Integer param) {
        // 创建kieSession
        KieSession kieSession = kieContainer.newKieSession(kieBaseName + "-session");
        StringBuilder resultInfo = new StringBuilder();
        kieSession.setGlobal("resultInfo", resultInfo);
        kieSession.insert(param);
        kieSession.fireAllRules();
        kieSession.dispose();
        return resultInfo.toString();
    }

    public KieContainer getKieContainer() {
        return kieContainer;
    }
}
