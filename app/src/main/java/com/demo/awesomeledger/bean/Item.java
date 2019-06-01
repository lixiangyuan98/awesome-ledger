package com.demo.awesomeledger.bean;

import com.demo.awesomeledger.util.ItemKind;
import com.demo.awesomeledger.util.ItemType;

import java.util.Date;

/* 账单条目 */
public class Item {

    private Integer id;
    private Date date;
    private ItemType itemType;
    private ItemKind itemKind;
    private Double money;
    private String comment;

    public Item(Integer id, Date date, ItemType itemType, ItemKind itemKind, Double money, String comment) {
        this.id = id;
        this.date = date;
        this.itemType = itemType;
        this.itemKind = itemKind;
        this.money = money;
        this.comment = comment;
    }

    public Item(Date date, ItemType itemType, ItemKind itemKind, Double money, String comment) {
        this.date = date;
        this.itemType = itemType;
        this.itemKind = itemKind;
        this.money = money;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemKind getItemKind() {
        return itemKind;
    }

    public void setItemKind(ItemKind itemKind) {
        this.itemKind = itemKind;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
