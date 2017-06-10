package com.example;

import com.example.table.Altitude;
import com.example.table.Project;
import com.xiaoleilu.hutool.lang.Console;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by ty on 2017/6/8.
 */

public class Bridge {


    public static void main(String[] args) {
        DBManager db = DBManager.getInstance();
        /*先完善bean的信息*/
        String querySQL = "select id,name from " + Project.tableName;
        PreparedStatement pstmt = db.getPreparedStatement(querySQL//传入控制结果集可滚动、可更新的参数
                , ResultSet.TYPE_SCROLL_INSENSITIVE
                , ResultSet.CONCUR_UPDATABLE);
        try {
            ResultSet rs = pstmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            for (int i = 1; i <= rowCount; i++) {
                rs.absolute(i);
                if ("杭深线".matches(".*" + rs.getString(2) + ".*")) {

                    Console.log(rs.getInt(1));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static DBManager mDBManager;

    public static void storeExcel2DB(AltitudeBean bean) {
        DBManager db = DBManager.getInstance();
        setBean(bean, db);//完善Bean类

        String insertManySQL = "insert into " + Altitude.tableName +
                "(" +
                "`" + Altitude.MILENUM + "`," +
                "`" + Altitude.MEASURE_TIME + "`," +
                "`" + Altitude.ALTITUDE + "`," +
                "`" + Altitude.GMT_CREATE + "`," +
                "`" + Altitude.GMT_MODIFIED + "`," +
                "`" + Altitude.LEVEL + "`," +
                "`" + Altitude.ADDR_WORK + "`," +
                "`" + Altitude.PROJECT_ID + "`," +
                "`" + Altitude.ADDR_DOT + "`" +
                ") " +
                "values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pSmt = db.getPreparedStatement(insertManySQL);
        if (pSmt == null) {
            try {
                throw new Exception("PreparedStatement为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Iterator it = bean.getmData().keySet().iterator();//测量周期的迭代器
        int v6 = bean.getLevel();
        String v7 = bean.getAddrWork();
        int v8 = bean.getProjectID();
        String v9 = bean.getAddrDot();
        while (it.hasNext()) {
            Date d = (Date) it.next();//某个测量日期
            long v2 = d.getTime() / 1000;//测量时间戳,java中生成的时间戳精确到毫秒级别，而unix中精确到秒级别
            TreeMap<Double, Double> data = bean.getmData().get(d);//里程号以及对应的高程值
            Iterator itt = data.keySet().iterator();//里程号的迭代器
            while (itt.hasNext()) {
                Double v1 = (Double) itt.next();//里程号
                Double v3 = data.get(v1);//高程值
                long v4 = System.currentTimeMillis() / 1000;//当前时间戳
                try {
                    pSmt.setDouble(1, v1);
                    pSmt.setLong(2, v2);
                    pSmt.setDouble(3, v3);
                    pSmt.setLong(4, v4);
                    pSmt.setLong(5, v4);
                    pSmt.setInt(6, v6);
                    pSmt.setString(7, v7);
                    pSmt.setInt(8, v8);
                    pSmt.setString(9, v9);
                    pSmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据表格内容获取数据库表示
     *
     * @param bean
     * @param db
     */
    private static void setBean(AltitudeBean bean, DBManager db) {
    /*先完善bean的信息*/
        String querySQL = "select " + Project.ID + "," + Project.NAME + " from " + Project.tableName;//查询工程ID,名称
        PreparedStatement pstmt = db.getPreparedStatement(querySQL//传入控制结果集可滚动、可更新的参数
                , ResultSet.TYPE_SCROLL_INSENSITIVE
                , ResultSet.CONCUR_UPDATABLE);
        try {
            ResultSet rs = pstmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            for (int i = 1; i <= rowCount; i++) {
                rs.absolute(i);
                if (bean.getProjectName().matches(".*" + rs.getString(2) + ".*")) {//如果表格中的工程名称匹配数据库中的名称
                    bean.setProjectID(rs.getInt(1));//设置Bean中的ID
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
