package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by ty on 2017/6/8.
 */

public class SheetBean {

    int pID;
    int pTypeID;
    int level;
    double[] projectRange;
    String addrWork;
    String projectName;
    String projectType;

    LinkedList<DotBean> list = new LinkedList<>();//数据集
    String sheetName;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public LinkedList<DotBean> getList() {
        return list;
    }

    public void setList(LinkedList<DotBean> list) {
        this.list = list;
    }


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

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public int getpTypeID() {
        return pTypeID;
    }

    public void setpTypeID(int pTypeID) {
        this.pTypeID = pTypeID;
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


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    static class DotBean {
        int id;
        Double milenum;//里程号
        Date date;//测量日期
        Double altitude;//高程值
        Double bamend;//修正值,使用本次高程值前修正，类似++i
        Double aAmend;//修正值,使用本次高程值后修正，类似i++
        boolean isInitPoint;//是否为起始点
        //        用sheetName替代
//        String address;//点的位置，路堤|底座板|轨道板
        String comment;//备注

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Double getMilenum() {
            return milenum;
        }

        public void setMilenum(Double milenum) {
            this.milenum = milenum;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Double getAltitude() {
            return altitude;
        }

        public void setAltitude(Double altitude) {
            this.altitude = altitude;
        }

        public Double getBamend() {
            return bamend;
        }

        public void setBamend(Double bamend) {
            this.bamend = bamend;
        }

        public Double getaAmend() {
            return aAmend;
        }

        public void setaAmend(Double aAmend) {
            this.aAmend = aAmend;
        }

        public boolean isInitPoint() {
            return isInitPoint;
        }

        public void setInitPoint(boolean initPoint) {
            isInitPoint = initPoint;
        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\nid:" + id)
                    .append("\ndate:" + new SimpleDateFormat("yyyy-MM-dd").format(date))
                    .append("\naltitude:" + altitude)
                    .append("\nbamend:" + bamend)
                    .append("\naAmend:" + aAmend)
                    .append("\nisInitPoint:" + isInitPoint)
//                    .append("\naddress:" + address)
                    .append("\ncomment:" + comment)
                    .toString();
        }
    }
}
