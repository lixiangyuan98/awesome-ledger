package com.demo.awesomeledger.gson;

import com.demo.awesomeledger.bean.Item;

import java.util.List;

public class SyncResponse {

    private List<Item> localDelete;
    private List<Item> localInsert;
    private List<Item> localUpdate;
    private List<String> remoteInsert;
    private List<String> remoteUpdate;

    public List<Item> getLocalDelete() {
        return localDelete;
    }

    public void setLocalDelete(List<Item> localDelete) {
        this.localDelete = localDelete;
    }

    public List<Item> getLocalInsert() {
        return localInsert;
    }

    public void setLocalInsert(List<Item> localInsert) {
        this.localInsert = localInsert;
    }

    public List<Item> getLocalUpdate() {
        return localUpdate;
    }

    public void setLocalUpdate(List<Item> localUpdate) {
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
}
