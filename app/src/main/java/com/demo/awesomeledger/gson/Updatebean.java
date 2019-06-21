package com.demo.awesomeledger.gson;

import com.demo.awesomeledger.bean.Item;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Updatebean {

    private List<DataBean> data;

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public Updatebean(){ data = new ArrayList<>(); }

    public List<DataBean> getData() { return data;}

    public void setData(Item item){
        String pattern = "YYYY-MM-dd'T'HH:mm:ssXXX";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String update = sdf.format(item.getUpdatedAt());
        String Dedate = null;
        String date = sdf.format(item.getDate());
        if(item.getDeletedAt() != null) {
            Dedate = sdf.format(item.getDeletedAt());
        } else {
            Dedate = null;
        }
        String Crdate = sdf.format(item.getCreatedAt());
        this.data.add(new DataBean(item.getUuid(), update, Dedate, Crdate, date, item.getMoney(), item.getAddress(), item.getComment(), item.getItemKind().getKind(), item.getItemType().getType()));
    }

    public static class DataBean {
        /**
         * uuid : uuid1
         * createdAt : 2019-06-01T04:12:01+08:00
         * updatedAt : 2019-06-01T04:12:01+08:00
         * deletedAt : 2019-06-01T04:12:01+08:00
         * date : 2019-06-01T04:12:01+08:00
         * itemType : outgoing
         * itemKind : FOOD
         * address :
         * money : 100.2
         * comment :
         */

        @SerializedName("uuid")
        private String uuidX;
        @SerializedName("createdAt")
        private String createdAtX;
        @SerializedName("updatedAt")
        private String updatedAtX;
        @SerializedName("deletedAt")
        private String deletedAtX;
        @SerializedName("date")
        private String dateX;
        @SerializedName("itemType")
        private String itemTypeX;
        @SerializedName("itemKind")
        private String itemKindX;
        @SerializedName("address")
        private String addressX;
        @SerializedName("money")
        private double moneyX;
        @SerializedName("comment")
        private String commentX;

        public DataBean(String uuid, String updatedAt, String deletedAt, String createdAt, String date, double money, String address, String comment, String itemKind, String itemType) {
            this.uuidX = uuid;
            this.updatedAtX = updatedAt;
            this.deletedAtX = deletedAt;
            this.createdAtX = createdAt;
            this.dateX = date;
            this.itemKindX = itemKind;
            this.itemTypeX = itemType;
            this.addressX = address;
            this.moneyX = money;
            this.commentX = comment;
        }

        public String getUuidX() {
            return uuidX;
        }

        public void setUuidX(String uuidX) {
            this.uuidX = uuidX;
        }

        public String getCreatedAtX() {
            return createdAtX;
        }

        public void setCreatedAtX(String createdAtX) {
            this.createdAtX = createdAtX;
        }

        public String getUpdatedAtX() {
            return updatedAtX;
        }

        public void setUpdatedAtX(String updatedAtX) {
            this.updatedAtX = updatedAtX;
        }

        public String getDeletedAtX() {
            return deletedAtX;
        }

        public void setDeletedAtX(String deletedAtX) {
            this.deletedAtX = deletedAtX;
        }

        public String getDateX() {
            return dateX;
        }

        public void setDateX(String dateX) {
            this.dateX = dateX;
        }

        public String getItemTypeX() {
            return itemTypeX;
        }

        public void setItemTypeX(String itemTypeX) {
            this.itemTypeX = itemTypeX;
        }

        public String getItemKindX() {
            return itemKindX;
        }

        public void setItemKindX(String itemKindX) {
            this.itemKindX = itemKindX;
        }

        public String getAddressX() {
            return addressX;
        }

        public void setAddressX(String addressX) {
            this.addressX = addressX;
        }

        public double getMoneyX() {
            return moneyX;
        }

        public void setMoneyX(double moneyX) {
            this.moneyX = moneyX;
        }

        public String getCommentX() {
            return commentX;
        }

        public void setCommentX(String commentX) {
            this.commentX = commentX;
        }
    }
}
