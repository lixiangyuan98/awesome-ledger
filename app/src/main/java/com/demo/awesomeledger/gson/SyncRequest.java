package com.demo.awesomeledger.gson;

import com.demo.awesomeledger.bean.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SyncRequest {

    private List<DataBean> data;

    public SyncRequest() {
        data = new ArrayList<>();
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public void setData(Item item) {
        String pattern = "YYYY-MM-dd'T'HH:mm:ssXXX";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String update = sdf.format(item.getUpdatedAt());
        String Dedate = null;
        if(item.getDeletedAt() != null) {
            Dedate = sdf.format(item.getDeletedAt());
        } else {
            Dedate = null;
        }
        String Crdate = sdf.format(item.getCreatedAt());
        this.data.add(new DataBean(item.getUuid(), update, Dedate, Crdate));
    }

    public static class DataBean {
        /**
         * uuid : uuid1
         * updatedAt : 2019-06-01T04:12:01+08:00
         * deletedAt : 2019-06-01T04:12:01+08:00
         * createdAt : 2019-06-01T04:12:01+08:00,
         */

        private String uuid;
        private String updatedAt;
        private String deletedAt;
        private String createdAt;

        public DataBean(String uuid, String updatedAt, String deletedAt, String createdAt) {
            this.uuid = uuid;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;
            this.createdAt = createdAt;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }
        public String getcreatedAt() {
            return createdAt;
        }

        public void setcreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
    @Override
    public String toString() {
        String str = new String();
        for(DataBean dataBean : getData()){
            str = str + " uuid: " + dataBean.getUuid() +
                        " updatedAt " + dataBean.getUpdatedAt() +
                        " deletedAt " + dataBean.getDeletedAt() +
                        " createdAt " + dataBean.getcreatedAt();
        }

        return "SyncRequest [" +str +"]";
    }
}
