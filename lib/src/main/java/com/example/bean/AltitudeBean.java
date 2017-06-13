package com.example.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ty on 2017/6/13.
 */

@Table("t_altitude")
public class AltitudeBean {
    @Id
    @ColDefine(unsigned = true, width = 9)
    private int id;

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "测量日期")
    private Date gmtMeasure;

    @Column(hump = true)
    @ColDefine(type = ColType.FLOAT, width = 18, precision = 5)
    @Comment(value = "里程号")
    private BigDecimal mileNo;

    @Column(hump = true)
    @Comment(value = "高程值")
    @ColDefine(type = ColType.FLOAT, width = 18, precision = 5)
    private BigDecimal height;

    @Column(hump = true)
    @Comment(value = "前置修正值")
    @ColDefine(type = ColType.FLOAT, width = 18, precision = 5)
    private BigDecimal preAmend;//修正值,使用本次高程值前修正，类似++i

    @Column(hump = true)
    @Comment(value = "后置修正值")
    @ColDefine(type = ColType.FLOAT, width = 18, precision = 5)
    private BigDecimal rearAmend;//修正值,使用本次高程值后修正，类似i++

    @Column(hump = true)
    @Comment(value = "是否为基准点")
    @ColDefine(unsigned = true)
    private boolean isBasepoint;

    @Column(hump = true)
    @Comment(value = "点的位置，路堤|底座板|轨道板")
    private String onWhat;

    @Column(hump = true)
    @Comment(value = "备注")
    private String comment;//

    @Column(hump = true)
    @Comment(value = "测点号")
    private String measureNo;

    @Column(hump = true)
    @Comment(value = "CP点号")
    private String cpNo;

    @Column(hump = true)
    @Comment(value = "外键,工程ID")
    private String pkPid;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 2)
    @Comment(value = "外键,施工类型ID")
    private int pkid;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 4)
    @Comment(value = "所属重点段")
    private int pkConcernid;

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "创建日期")
    private Date gmtCreate;

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "更改日期")
    private Date gmtModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getGmtMeasure() {
        return gmtMeasure;
    }

    public void setGmtMeasure(Date gmtMeasure) {
        this.gmtMeasure = gmtMeasure;
    }

    public BigDecimal getMileNo() {
        return mileNo;
    }

    public void setMileNo(BigDecimal mileNo) {
        this.mileNo = mileNo;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getPreAmend() {
        return preAmend;
    }

    public void setPreAmend(BigDecimal preAmend) {
        this.preAmend = preAmend;
    }

    public BigDecimal getRearAmend() {
        return rearAmend;
    }

    public void setRearAmend(BigDecimal rearAmend) {
        this.rearAmend = rearAmend;
    }

    public boolean isBasepoint() {
        return isBasepoint;
    }

    public void setBasepoint(boolean basepoint) {
        isBasepoint = basepoint;
    }

    public String getOnWhat() {
        return onWhat;
    }

    public void setOnWhat(String onWhat) {
        this.onWhat = onWhat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMeasureNo() {
        return measureNo;
    }

    public void setMeasureNo(String measureNo) {
        this.measureNo = measureNo;
    }

    public String getCpNo() {
        return cpNo;
    }

    public void setCpNo(String cpNo) {
        this.cpNo = cpNo;
    }

    public String getPkPid() {
        return pkPid;
    }

    public void setPkPid(String pkPid) {
        this.pkPid = pkPid;
    }

    public int getPkid() {
        return pkid;
    }

    public void setPkid(int pkid) {
        this.pkid = pkid;
    }

    public int getPkConcernid() {
        return pkConcernid;
    }

    public void setPkConcernid(int pkConcernid) {
        this.pkConcernid = pkConcernid;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
//    int pID;
//    int pTypeID;
//    int level;
//    int foucusID;
//    double[] projectRange;
//    String addrWork;
//    String projectName;
//    String projectType;
//
//    LinkedList<SheetBean.DotBean> list = new LinkedList<>();//数据集
//    String sheetName;
}
