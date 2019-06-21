package com.demo.awesomeledger.gson;

import com.demo.awesomeledger.bean.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SyncRequest {

    private List<Data> data = new ArrayList<>();

    private List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public void setData(Item item) {
        data.add(new Data(item));
    }

    class Data {
        private String uuid;
        private Date updatedAt;
        private Date deletedAt;
        private Date createdAt;

        Data(Item item) {
            this.uuid = item.getUuid();
            this.updatedAt = item.getUpdatedAt();
            this.deletedAt = item.getDeletedAt();
            this.createdAt = item.getCreatedAt();
        }

        String getUuid() {
            return uuid;
        }

        void setUuid(String uuid) {
            this.uuid = uuid;
        }

        Date getUpdatedAt() {
            return updatedAt;
        }

        void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        Date getDeletedAt() {
            return deletedAt;
        }

        void setDeletedAt(Date deletedAt) {
            this.deletedAt = deletedAt;
        }

        Date getCreatedAt() {
            return createdAt;
        }

        void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Data dataBean : getData()){
            stringBuilder
                    .append(" uuid: ")
                    .append(dataBean.getUuid())
                    .append(" updatedAt ")
                    .append(dataBean.getUpdatedAt())
                    .append(" deletedAt ")
                    .append(dataBean.getDeletedAt())
                    .append(" createdAt ")
                    .append(dataBean.getCreatedAt());
        }
        return "SyncRequest [" +stringBuilder.toString() +"]";
    }
}
