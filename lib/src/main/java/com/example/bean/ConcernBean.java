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
 * 重点段表
 */

@Table("t_concern")
public class ConcernBean {
    @Id
    @ColDefine(unsigned = true, width = 2)
    private int id;

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "创建日期")
    private Date gmtCreate;

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "更改日期")
    private Date gmtModified;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 2)
    @Comment(value = "外键,施工类型ID")
    private int pkid;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 18, precision = 5)
    @Comment(value = "范围")
    private BigDecimal start;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 18, precision = 5)
    @Comment(value = "范围")
    private BigDecimal end;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 1)
    @Comment(value = "测量等级")
    private int level;

    @Column(hump = true)
    @Comment(value = "施工地点")
    private String address;

    private String projectName;
    private String projectType;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPkid() {
        return pkid;
    }

    public void setPkid(int pkid) {
        this.pkid = pkid;
    }

    public BigDecimal getStart() {
        return start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getEnd() {
        return end;
    }

    public void setEnd(BigDecimal end) {
        this.end = end;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    //    int foucusID;
//    double[] projectRange;
//    String addrWork;
//    String projectName;
//    String projectType;
//
//    LinkedList<SheetBean.DotBean> list = new LinkedList<>();//数据集
//    String sheetName;
}
