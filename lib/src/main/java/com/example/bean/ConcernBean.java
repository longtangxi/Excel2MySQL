package com.example.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import java.util.Date;

/**
 * Created by ty on 2017/6/13.
 * 重点段表
 */

@Table("t_concern")
@TableIndexes(@Index(name = "uk_pid_start_end", fields = {"pkPid", "start", "end"}, unique = true))
public class ConcernBean {
    @Id
    @ColDefine(unsigned = true, width = 2)
    private int id;
    @Name
    private String name;

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
    @Comment(value = "外键,工程ID")
    private int pkPid;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 2)
    @Comment(value = "外键,施工类型ID")
    private int pkTypeid;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 18, precision = 5)
    @Comment(value = "范围")
    private long start;

    @Column(hump = true)
    @ColDefine(unsigned = true, width = 18, precision = 5)
    @Comment(value = "范围")
    private long end;

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

    public int getPkPid() {
        return pkPid;
    }

    public void setPkPid(int pkPid) {
        this.pkPid = pkPid;
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

    public int getPkTypeid() {
        return pkTypeid;
    }

    public void setPkTypeid(int pkTypeid) {
        this.pkTypeid = pkTypeid;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        return new StringBuilder().append(ConcernBean.class.getSimpleName())
                .append("[")
                .append("id:" + id)
//                .append(",gmtCreate:" + new SimpleDateFormat("yyyy-MM-dd").format(gmtCreate.getTime()))
//                .append(",gmtModified:" + new SimpleDateFormat("yyyy-MM-dd").format(gmtModified.getTime()))
                .append(",name:" + name)
                .append(",projectName:" + projectName)
                .append(",pkPid:" + pkPid)
                .append(",projectType:" + projectType)
                .append(",pkTypeid:" + pkTypeid)
                .append(",level:" + level)
                .append(",start:" + start)
                .append(",end:" + end)
                .append(",address:" + address)
                .toString();
    }
}