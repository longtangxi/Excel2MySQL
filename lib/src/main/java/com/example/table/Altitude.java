package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */

import com.example.DBManager;

/**
 * 高程表
 */
public class Altitude extends BaseTable {
    public static String tableName = Altitude.class.getSimpleName().toLowerCase();
    public static String MILENUM = "mile_num";
    public static String ALTITUDE = "altitude";
    public static String AMEND_VALUE = "amend_value";
    public static String MEASURE_TIME = "measure_time";
    public static String SUBMIT_TIME = "submit_time";
    public static String IS_INIT = "is_initpoint";
    public static String REMARKS = "remarks";
    public static String ADDR_WORK = "addr_work";
    public static String ADDR_DOT = "addr_dot";
    public static String LEVEL = "level";
    public static String PROJECT_ID = "pro_id";


    public static String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
            "( " + ID + "  bigint unsigned auto_increment primary key, " +
            MILENUM + " int NOT NULL COMMENT '里程号', " +
            ALTITUDE + " decimal(18,5) COMMENT '高程值', " +
            AMEND_VALUE + " decimal(18,5) COMMENT '高程修正值'," +
            MEASURE_TIME + " int(11) COMMENT '测量时间', " +
            SUBMIT_TIME + " int(11) COMMENT '提交时间', " +
            GMT_CREATE + " int(11), " +
            GMT_MODIFIED + " int(11), " +
            ADDR_WORK + " varchar(40) COMMENT '施工地点', " +
            ADDR_DOT + " varchar(40) COMMENT '点号位置', " +
            LEVEL + " int(1) COMMENT '测量等级', " +
            IS_INIT + " tinyint unsigned COMMENT '是否为起始参考点' DEFAULT 0," +
            REMARKS + " varchar(100) COMMENT '备注'," +
            PROJECT_ID + " int COMMENT '工程ID'," +
            "constraint mileandtime_uk unique( " + MILENUM + "," + MEASURE_TIME + ")" +//点号及测量日期唯一性约束
            ")";

    public static void main(String[] args) {
        DBManager manager = DBManager.getInstance();
        try {
            manager.executeUpdate(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
