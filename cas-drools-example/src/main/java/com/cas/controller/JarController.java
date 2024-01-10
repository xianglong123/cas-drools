package com.cas.controller;

import com.cas.service.OrderDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午2:15 2024/1/8
 * @version: V1.0
 * @review:
 */
@RestController
public class JarController {

    @Resource
    private OrderDiscountService orderDiscountService;

    @PostMapping("/init")
    public ResponseEntity<?> init() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<?> test() {
        orderDiscountService.execute();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay() {
        orderDiscountService.pay();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
