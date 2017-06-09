package com.example;

import java.util.Date;
import java.util.TreeMap;

/**
 * Created by ty on 2017/6/8.
 */

public class AltitudeBean {

    int projectID;
    int projectTypeID;
    int level;
    double[] projectRange;
    String addrWork;
    String addrDot;
    String projectName;

    public String getAddrDot() {
        return addrDot;
    }

    public void setAddrDot(String addrDot) {
        this.addrDot = addrDot;
    }

    String projectType;

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public double[] getProjectRange() {
        return projectRange;
    }

    public void setProjectRange(double[] projectRange) {
        this.projectRange = projectRange;
    }

    //存储日期-点号-变形量
    TreeMap<Date, TreeMap<Double, Double>> mData = new TreeMap<>();

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getProjectTypeID() {
        return projectTypeID;
    }

    public void setProjectTypeID(int projectTypeID) {
        this.projectTypeID = projectTypeID;
    }

    public String getAddrWork() {
        return addrWork;
    }

    public void setAddrWork(String addrWork) {
        this.addrWork = addrWork;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
