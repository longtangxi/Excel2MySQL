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
    public static String PRE_AMEND = "pre_amend";
    public static String REAR_AMEND = "rear_amend";
    public static String MEASURE_TIME = "measure_time";
    public static String SUBMIT_TIME = "submit_time";
    public static String IS_INIT = "is_initpoint";
    public static String COMMENT = "comment";
    public static String ADDR_WORK = "addr_work";
    public static String ADDR_DOT = "addr_dot";
    public static String LEVEL = "level";
    public static String PROJECT_ID = "pro_id";
    public static String FOCUS_ID = "focus_id";
    public static String CPNUM = "cp_num";
    public static String MEASURENUM = "measure_num";


    static StringBuilder createTableSQL = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(tableName)
            .append("( ")
            .append(ID + "  bigint unsigned auto_increment primary key")
            .append("," + MILENUM + " decimal(18,5) NOT NULL COMMENT '里程号'")
            .append("," + ALTITUDE + " decimal(18,5) COMMENT '高程值'")
            .append("," + PRE_AMEND + " decimal(18,5) COMMENT '前置高程修正值'")
            .append("," + REAR_AMEND + " decimal(18,5) COMMENT '后置高程修正值'")
            .append("," + ADDR_WORK + " varchar(40) COMMENT '施工地点'")
            .append("," + ADDR_DOT + " varchar(40) COMMENT '点号位置'")
            .append("," + LEVEL + " tinyint unsigned COMMENT '测量等级'")
            .append("," + IS_INIT + " tinyint unsigned COMMENT '是否为起始参考点' DEFAULT 0")
            .append("," + COMMENT + " varchar(100) COMMENT '备注'")
            .append("," + PROJECT_ID + " tinyint unsigned COMMENT '工程ID'")
            .append("," + FOCUS_ID + " tinyint unsigned COMMENT '所属重点段ID'")
            .append("," + CPNUM + " varchar(100) COMMENT 'CP点号'")
            .append("," + MEASURENUM + " varchar(100) COMMENT '测点号'")
            .append("," + MEASURE_TIME + " datetime")
            .append("," + SUBMIT_TIME + " datetime")
            .append("," + GMT_CREATE + " datetime NOT NULL")
            .append("," + GMT_MODIFIED + " datetime NOT NULL")
            .append("," + "constraint milenum_measuretime_uk unique( " + MILENUM + "," + MEASURE_TIME + ")")
            .append(" )");

    public static void main(String[] args) {
        DBManager manager = DBManager.getInstance();
        try {
            manager.executeUpdate(createTableSQL.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
