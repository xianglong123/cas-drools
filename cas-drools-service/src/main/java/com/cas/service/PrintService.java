package com.cas.service;

import com.cas.crule.Product;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @description:
 * @author: xianglong
 * @create: 2024-01-09 17:37
 **/
@Service
@Transactional
public class PrintService {

    public void print(Product product) {
        System.out.println(product.getName() + "调用外部方法成功");
    }

}
