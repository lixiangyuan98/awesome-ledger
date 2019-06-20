package com.demo.awesomeledger.Gson;

import java.util.List;
import java.util.Date;

public class syncResponse {

    /**
     * result : {"localDelete":[{"ID":1,"CreatedAt":"","UpdateAt":"","DeletedAt":"","UUID":"","Date":"","ItemType":"","ItemKind":"","Address":"","Money":123,"Comment":""}],"localInsert":[{"ID":1,"CreatedAt":"","UpdateAt":"","DeletedAt":"","UUID":"","Date":"","ItemType":"","ItemKind":"","Address":"","Money":123,"Comment":""}],"localUpdate":[{"ID":1,"CreatedAt":"","UpdateAt":"","DeletedAt":"","UUID":"","Date":"","ItemType":"","ItemKind":"","Address":"","Money":123,"Comment":""}],"remoteInsert":["",""],"remoteUpdate":["",""]}
     */
    private dataBean data;

    public dataBean getData() {
        return data;
    }

    public void setResult(dataBean data) {
        this.data = data;
    }

    public static class dataBean {
        private List<LocalDeleteBean> localDelete;
        private List<LocalInsertBean> localInsert;
        private List<LocalUpdateBean> localUpdate;
        private List<String> remoteInsert;
        private List<String> remoteUpdate;

        public List<LocalDeleteBean> getLocalDelete() {
            return localDelete;
        }

        public void setLocalDelete(List<LocalDeleteBean> localDelete) {
            this.localDelete = localDelete;
        }

        public List<LocalInsertBean> getLocalInsert() {
            return localInsert;
        }

        public void setLocalInsert(List<LocalInsertBean> localInsert) {
            this.localInsert = localInsert;
        }

        public List<LocalUpdateBean> getLocalUpdate() {
            return localUpdate;
        }

        public void setLocalUpdate(List<LocalUpdateBean> localUpdate) {
            this.localUpdate = localUpdate;
        }

        public List<String> getRemoteInsert() {
            return remoteInsert;
        }

        public void setRemoteInsert(List<String> remoteInsert) {
            this.remoteInsert = remoteInsert;
        }

        public List<String> getRemoteUpdate() {
            return remoteUpdate;
        }

        public void setRemoteUpdate(List<String> remoteUpdate) {
            this.remoteUpdate = remoteUpdate;
        }

        public static class LocalDeleteBean {
            /**
             * ID : 1
             * CreatedAt :
             * UpdateAt :
             * DeletedAt :
             * UUID :
             * Date :
             * ItemType :
             * ItemKind :
             * Address :
             * Money : 123
             * Comment :
             */

            private int ID;
            private Date CreatedAt;
            private Date UpdateAt;
            private Date DeletedAt;
            private String UUID;
            private Date Date;
            private String ItemType;
            private String ItemKind;
            private String Address;
            private double Money;
            private String Comment;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public Date getCreatedAt() {
                return CreatedAt;
            }

            public void setCreatedAt(Date CreatedAt) {
                this.CreatedAt = CreatedAt;
            }

            public Date getUpdateAt() {
                return UpdateAt;
            }

            public void setUpdateAt(Date UpdateAt) {
                this.UpdateAt = UpdateAt;
            }

            public Date getDeletedAt() {
                return DeletedAt;
            }

            public void setDeletedAt(Date DeletedAt) {
                this.DeletedAt = DeletedAt;
            }

            public String getUUID() {
                return UUID;
            }

            public void setUUID(String UUID) {
                this.UUID = UUID;
            }

            public Date getDate() {
                return Date;
            }

            public void setDate(Date Date) {
                this.Date = Date;
            }

            public String getItemType() {
                return ItemType;
            }

            public void setItemType(String ItemType) {
                this.ItemType = ItemType;
            }

            public String getItemKind() {
                return ItemKind;
            }

            public void setItemKind(String ItemKind) {
                this.ItemKind = ItemKind;
            }

            public String getAddress() {
                return Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public double getMoney() {
                return Money;
            }

            public void setMoney(double Money) {
                this.Money = Money;
            }

            public String getComment() {
                return Comment;
            }

            public void setComment(String Comment) {
                this.Comment = Comment;
            }
        }

        public static class LocalInsertBean {
            /**
             * ID : 1
             * CreatedAt :
             * UpdateAt :
             * DeletedAt :
             * UUID :
             * Date :
             * ItemType :
             * ItemKind :
             * Address :
             * Money : 123
             * Comment :
             */

            private int ID;
            private Date CreatedAt;
            private Date UpdateAt;
            private Date DeletedAt;
            private String UUID;
            private Date Date;
            private String ItemType;
            private String ItemKind;
            private String Address;
            private double Money;
            private String Comment;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public Date getCreatedAt() {
                return CreatedAt;
            }

            public void setCreatedAt(Date CreatedAt) {
                this.CreatedAt = CreatedAt;
            }

            public Date getUpdateAt() {
                return UpdateAt;
            }

            public void setUpdateAt(Date UpdateAt) {
                this.UpdateAt = UpdateAt;
            }

            public Date getDeletedAt() {
                return DeletedAt;
            }

            public void setDeletedAt(Date DeletedAt) {
                this.DeletedAt = DeletedAt;
            }

            public String getUUID() {
                return UUID;
            }

            public void setUUID(String UUID) {
                this.UUID = UUID;
            }

            public Date getDate() {
                return Date;
            }

            public void setDate(Date Date) {
                this.Date = Date;
            }

            public String getItemType() {
                return ItemType;
            }

            public void setItemType(String ItemType) {
                this.ItemType = ItemType;
            }

            public String getItemKind() {
                return ItemKind;
            }

            public void setItemKind(String ItemKind) {
                this.ItemKind = ItemKind;
            }

            public String getAddress() {
                return Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public double getMoney() {
                return Money;
            }

            public void setMoney(double Money) {
                this.Money = Money;
            }

            public String getComment() {
                return Comment;
            }

            public void setComment(String Comment) {
                this.Comment = Comment;
            }
        }

        public static class LocalUpdateBean {
            /**
             * ID : 1
             * CreatedAt :
             * UpdateAt :
             * DeletedAt :
             * UUID :
             * Date :
             * ItemType :
             * ItemKind :
             * Address :
             * Money : 123
             * Comment :
             */

            private int ID;
            private Date CreatedAt;
            private Date UpdateAt;
            private Date DeletedAt;
            private String UUID;
            private Date Date;
            private String ItemType;
            private String ItemKind;
            private String Address;
            private double Money;
            private String Comment;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public Date getCreatedAt() {
                return CreatedAt;
            }

            public void setCreatedAt(Date CreatedAt) {
                this.CreatedAt = CreatedAt;
            }

            public Date getUpdateAt() {
                return UpdateAt;
            }

            public void setUpdateAt(Date UpdateAt) {
                this.UpdateAt = UpdateAt;
            }

            public Date getDeletedAt() {
                return DeletedAt;
            }

            public void setDeletedAt(Date DeletedAt) {
                this.DeletedAt = DeletedAt;
            }

            public String getUUID() {
                return UUID;
            }

            public void setUUID(String UUID) {
                this.UUID = UUID;
            }

            public Date getDate() {
                return Date;
            }

            public void setDate(Date Date) {
                this.Date = Date;
            }

            public String getItemType() {
                return ItemType;
            }

            public void setItemType(String ItemType) {
                this.ItemType = ItemType;
            }

            public String getItemKind() {
                return ItemKind;
            }

            public void setItemKind(String ItemKind) {
                this.ItemKind = ItemKind;
            }

            public String getAddress() {
                return Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public double getMoney() {
                return Money;
            }

            public void setMoney(double Money) {
                this.Money = Money;
            }

            public String getComment() {
                return Comment;
            }

            public void setComment(String Comment) {
                this.Comment = Comment;
            }
        }
    }
}
