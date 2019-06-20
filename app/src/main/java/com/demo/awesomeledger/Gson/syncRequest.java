package com.demo.awesomeledger.Gson;

import java.util.List;
import java.util.Date;

public class syncRequest {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uuid : uuid1
         * updatedAt : 2019-06-01T04:12:01+08:00
         * deletedAt : 2019-06-01T04:12:01+08:00
         * createdAt : 2019-06-01T04:12:01+08:00,
         */

        private String uuid;
        private Date updatedAt;
        private Date deletedAt;
        private Date createdAt;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
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
        public Date getcreatedAt() {
            return createdAt;
        }

        public void setcreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }
}
