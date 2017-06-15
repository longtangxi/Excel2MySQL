package com.example;

import com.example.bean.AltitudeBean;
import com.example.bean.ConcernBean;
import com.example.bean.ProjectBean;
import com.xiaoleilu.hutool.lang.Console;

import org.nutz.dao.Dao;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by ty on 2017/6/8.
 */

public class Bridge {


    public static void main(String[] args) {

        Dao dao = DaoUtil.getDao();
        dao.create(ConcernBean.class, false);
        dao.create(AltitudeBean.class, false);
        dao.create(ProjectBean.class, false);

    }

    private static DBManager mDBManager;

    public static void storeExcel2DB(ConcernBean concernBean, LinkedList<AltitudeBean> points) {
        Console.log(concernBean.toString());
        Dao dao = DaoUtil.getDao();

        ConcernBean c = dao.fetch(ConcernBean.class, concernBean.getName());
        ProjectBean p = dao.fetch(ProjectBean.class, concernBean.getProjectName());
        if (c == null) {
            dao.insert(concernBean);
            Console.log("没有查询到指定的重点段，它的ID:" + c.toString());
        } else {
            Console.log("查询到指定的重点段" + c.toString());
        }
        if (p == null) {
            p = new ProjectBean();
            p.setName(concernBean.getProjectName());
            p.setGmtCreate(new Date());
            p.setGmtModified(new Date());
            dao.insert(p);
        }
        for (AltitudeBean bean : points) {
            bean.setPkConcernid(c.getId());
            bean.setPkPid(p.getId());
            bean.setGmtCreate(new Date());
            bean.setGmtModified(new Date());
        }
        dao.insert(points);

    }


}
