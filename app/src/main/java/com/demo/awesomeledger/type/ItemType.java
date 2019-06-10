package com.demo.awesomeledger.type;

/* 账单类型 */
public enum ItemType {
    OUTGOING("支出"),
    INCOME("收入");
    private String type;

    private ItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
