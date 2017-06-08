package com.example;

import java.util.Date;
import java.util.TreeMap;

/**
 * Created by ty on 2017/6/8.
 */

public class AltitudeBean {

    int projectID;
    int projectType;
    String address;
    int level;
    //存储日期-点号-变形量
    TreeMap<Date, TreeMap<Double, Double>> mData = new TreeMap<>();

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TreeMap<Date, TreeMap<Double, Double>> getmData() {
        return mData;
    }

    public void setmData(TreeMap<Date, TreeMap<Double, Double>> mData) {
        this.mData = mData;
    }
}
