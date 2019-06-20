package com.demo.awesomeledger.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class insertbean {

    private List<DataBean> data;

    public void setData(List<DataBean> data) {
        this.data = data;
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
        private Date createdAtX;
        @SerializedName("updatedAt")
        private Date updatedAtX;
        @SerializedName("deletedAt")
        private Date deletedAtX;
        @SerializedName("date")
        private Date dateX;
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

        public String getUuidX() {
            return uuidX;
        }

        public void setUuidX(String uuidX) {
            this.uuidX = uuidX;
        }

        public Date getCreatedAtX() {
            return createdAtX;
        }

        public void setCreatedAtX(Date createdAtX) {
            this.createdAtX = createdAtX;
        }

        public Date getUpdatedAtX() {
            return updatedAtX;
        }

        public void setUpdatedAtX(Date updatedAtX) {
            this.updatedAtX = updatedAtX;
        }

        public Date getDeletedAtX() {
            return deletedAtX;
        }

        public void setDeletedAtX(Date deletedAtX) {
            this.deletedAtX = deletedAtX;
        }

        public Date getDateX() {
            return dateX;
        }

        public void setDateX(Date dateX) {
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
