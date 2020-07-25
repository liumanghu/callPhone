package com.example.callphone.model;

public class Record {
    private int id;
    private String name;
    private String phoneNum;
    private String dataTime;

    public Record(int id,String phoneNum, String dataTime) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.dataTime = dataTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
