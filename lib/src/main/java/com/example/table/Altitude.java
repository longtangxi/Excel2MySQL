package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */

public class Altitude {
    public static String tableName = "altitude_table";
    public static String ID= "id";
    public static String DOT_NUM = "dot_num";
    public static String ALTITUDE = "altitude";
    public static String AMEND_VALUE = "amend_value";
    public static String MEASURE_TIME = "measure_time";
    public static String SUBMIT_TIME = "submit_time";
    public static String CREATE_TIME = "create_time";
    public static String UPDATE_TIME = "update_time";
    public static String IS_INIT = "is_initpoint";
    public static String REMARKS = "remarks";


    public static String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
            "( " + ID + " int auto_increment primary key, " +
            DOT_NUM + " int NOT NULL COMMENT '点号', " +
            ALTITUDE + " decimal(18,5) COMMENT '高程值', " +
            AMEND_VALUE + " decimal(18,5) COMMENT '高程修正值'," +
            MEASURE_TIME + " int(13) COMMENT '测量时间', " +
            SUBMIT_TIME + " int(13) COMMENT '提交时间', " +
            CREATE_TIME + " int(13), " +
            UPDATE_TIME + " int(13), " +
            IS_INIT + " boolean COMMENT '是否为起始参考点' DEFAULT 0," +
            REMARKS + " varchar(100) COMMENT '备注');";
}
