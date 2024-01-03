package com.cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: ${description}
 * @author: xianglong
 * @create: ${YEAR}-${MONTH}-${DAY} ${HOUR}:${MINUTE}
 **/
@SpringBootApplication(scanBasePackages = "com.cas")
public class DroolsApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(DroolsApplication.class, args);
            System.out.println("测试环境启动成功！！！！");
        } catch (Exception e) {
            System.out.println("测试环境启动失败！！！！");
        }
    }
}