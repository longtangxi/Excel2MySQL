package com.example.bean;


import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Created by ty on 2017/6/13.
 */

@Table("t_project")
public class ProjectBean {
    @Id
    @ColDefine(unsigned = true, width = 9)
    private int id;

    @Name
    private String name;

    @Column(hump = true)
    @Comment(value = "备注")
    private String comment;//

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
