package com.demo.awesomeledger.bean;

import com.demo.awesomeledger.type.ItemKind;
import com.demo.awesomeledger.type.ItemType;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/* 账单条目 */
public class Item {

    @SerializedName("ID")
    private Integer id;
    @SerializedName("UUID")
    private String uuid;
    @SerializedName("Date")
    private Date date;
    @SerializedName("ItemType")
    private ItemType itemType;
    @SerializedName("ItemKind")
    private ItemKind itemKind;
    @SerializedName("Address")
    private String address;
    @SerializedName("Money")
    private Double money;
    @SerializedName("Comment")
    private String comment;
    @SerializedName("CreatedAt")
    private Date createdAt;
    @SerializedName("UpdatedAt")
    private Date updatedAt;
    @SerializedName("DeletedAt")
    private Date deletedAt;

    // 新建Item时调用
    public Item() {
        this.uuid = UUID.randomUUID().toString();
        this.date = new Date();
        this.address = "";
        this.money = 0D;
        this.comment = "";
        this.createdAt = date;
        this.updatedAt = date;
        this.deletedAt = null;
    }

    // 从数据库读取Item时调用
    public Item(Integer id, String uuid, Date date, ItemType itemType, ItemKind itemKind, String address,
                Double money, String comment, Date createdAt, Date updatedAt, Date deletedAt) {
        this.id = id;
        this.uuid = uuid;
        this.date = date;
        this.itemType = itemType;
        this.itemKind = itemKind;
        this.address = address;
        this.money = money;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
