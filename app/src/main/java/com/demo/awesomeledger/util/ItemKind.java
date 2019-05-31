package com.demo.awesomeledger.util;

/* 账单消费类型 */
public enum ItemKind {

    FOOD("餐饮"),
    CLOTHES("服饰"),
    SHOES("鞋子"),
    HOUSE("住房"),
    TRANSPORT("交通"),
    MEDICAL("医疗"),
    SOCIAL("社交"),
    ENTERTAINMENT("娱乐"),
    SHOPPING("购物"),
    SPORT("运动");

    private String kind;

    private ItemKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }
}
