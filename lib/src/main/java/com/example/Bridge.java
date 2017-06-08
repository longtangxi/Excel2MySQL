package com.example;

import com.example.table.Altitude;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by ty on 2017/6/8.
 */

public class Bridge {


    private static DBManager mDBManager;

    public static void storeExcel2DB(AltitudeBean bean) {
        DBManager db = DBManager.getInstance();
        /*先完善bean的信息*/




        String insertManySQL = "insert into " + Altitude.tableName +
                "(" +
                "`" + Altitude.DOT_NUM + "`," +
                "`" + Altitude.MEASURE_TIME + "`," +
                "`" + Altitude.ALTITUDE + "`," +
                "`" + Altitude.CREATE_TIME + "`," +
                "`" + Altitude.UPDATE_TIME + "`" +
                ") " +
                "values(?,?,?,?,?)";
        PreparedStatement pSmt = db.getPreparedStatement(insertManySQL);
        if (pSmt == null) {
            try {
                throw new Exception("PreparedStatement为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Iterator it = bean.getmData().keySet().iterator();//测量周期的迭代器
        while (it.hasNext()) {
            Date d = (Date) it.next();//某个测量日期
            long measure_time = d.getTime() / 1000;//测量时间戳,java中生成的时间戳精确到毫秒级别，而unix中精确到秒级别
            TreeMap<Double, Double> data = bean.getmData().get(d);//里程号以及对应的高程值
            Iterator itt = data.keySet().iterator();//里程号的迭代器
            while (itt.hasNext()) {
                Double milenum = (Double) itt.next();//里程号
                Double altitude = data.get(milenum);//高程值
                long curTime = System.currentTimeMillis() / 1000;//当前时间戳
                try {
                    pSmt.setDouble(1, milenum);
                    pSmt.setLong(2, measure_time);
                    pSmt.setDouble(3, altitude);
                    pSmt.setLong(4, curTime);
                    pSmt.setLong(5, curTime);
                    pSmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
