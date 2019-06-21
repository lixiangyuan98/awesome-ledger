package com.demo.awesomeledger.gson;

import com.demo.awesomeledger.bean.Item;

import java.util.ArrayList;
import java.util.List;

public class InsertRequest {

    private List<Item> data = new ArrayList<>();

    public void setData(List<Item> data) {
        this.data = data;
    }

    public List<Item> getData() { return data;}

    public void setData(Item item){
        data.add(item);
    }
}
