package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */


import com.example.DBManager;

/**
 * 高程表
 */
public class Project extends BaseTable {
    public static String tableName = Project.class.getSimpleName().toLowerCase();
    public static String NAME = "name";
    public static String CREATE_TIME = "create_time";
    public static String UPDATE_TIME = "update_time";


    public static String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
            "( " + ID + " tinyint unsigned auto_increment primary key, " +
            NAME + " varchar(100) NOT NULL COMMENT '名称', " +
            GMT_CREATE + " int(11), " +
            GMT_MODIFIED + " int(11) " +
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
