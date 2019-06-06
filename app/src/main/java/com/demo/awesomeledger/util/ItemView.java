package com.demo.awesomeledger.util;

public class ItemView {
    private String time;
    private String type;
    private String kind;
    private double amont;

    public void settime(String time) {
        this.time = time;
    }
    public void settype(String type) {
        this.type = type;
    }
    public void setkind(String kind) {
        this.kind = kind;
    }
    public void setamont(double amont) {
        this.amont = amont;
    }
    public String gettime() {
        return time;
    }
    public String gettype() {
        return type;
    }
    public String getkind() {
        return kind;
    }
    public double getamont() {
        return amont;
    }
}
