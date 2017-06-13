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
    private Date gmt_date;

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

    //        用sheetName替代
    //        String address;//点的位置，路堤|底座板|轨道板

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
    @Comment(value = "外键,施工类型ID")
    private String pkid;


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
