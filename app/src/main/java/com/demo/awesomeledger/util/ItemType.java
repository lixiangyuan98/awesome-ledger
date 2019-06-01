package com.demo.awesomeledger.util;

/* 账单类型 */
public enum ItemType {
    INCOME("收入"),
    OUTGOING("支出");

    private String type;

    private ItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
