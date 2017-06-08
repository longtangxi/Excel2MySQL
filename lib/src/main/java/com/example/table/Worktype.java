package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */


import com.example.DBManager;

/**
 * 高程表
 */
public class Worktype {
    public static String tableName = "worktype_table";
    public static String ID= "id";
    public static String NAME = "name";
    public static String CREATE_TIME = "create_time";
    public static String UPDATE_TIME = "update_time";


    public static String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
            "( " + ID + " int auto_increment primary key, " +
            NAME + " varchar(100) NOT NULL COMMENT '名称', " +
            CREATE_TIME + " int(11), " +
            UPDATE_TIME + " int(11)" +
            ")";

    public static void main(String[] args){
        DBManager manager = DBManager.getInstance();
        try {
            manager.executeUpdate(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
