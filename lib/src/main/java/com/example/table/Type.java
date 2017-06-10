package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */


import com.example.DBManager;

/**
 * 高程表
 */
public class Type extends BaseTable{
    public static String tableName = Type.class.getSimpleName().toLowerCase();
    public static String NAME = "name";


    public static String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
            "( " + ID + " tinyint unsigned auto_increment primary key, " +
            NAME + " varchar(100) NOT NULL COMMENT '名称', " +
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
