package com.cas.bo;

import com.cas.enums.CustomerType;

public class OrderRequest {
    /**
     * 客户号
     */
    private String customerNumber;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 订单金额
     */
    private Integer amount;
    /**
     * 客户类型
     */
    private CustomerType customerType;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}
