package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */


import com.example.DBManager;
import com.xiaoleilu.hutool.lang.Console;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 高程表
 */
public class Project extends BaseTable {
    //测试新
    public static String tableName = Project.class.getSimpleName().toLowerCase();
    public static String NAME = "name";


    public static void main(String[] args) {

        StringBuilder createTableSQL = new StringBuilder();

        createTableSQL.append("CREATE TABLE IF NOT EXISTS " + tableName)
                .append("( ")
                .append(ID + " tinyint unsigned auto_increment primary key")
                .append("," + NAME + " varchar(100) NOT NULL COMMENT '名称'")
                .append("," + GMT_CREATE + " datetime")
                .append("," + GMT_MODIFIED + " datetime")
                .append(" )");

//            "( "+ID +" tinyint unsigned auto_increment primary key, "+
//    NAME +" varchar(100) NOT NULL COMMENT '名称', "+
//    GMT_CREATE +" datetime, "+
//    GMT_MODIFIED +" datetime "+
//            ")";

        DBManager manager = DBManager.getInstance();
        try {
            Date d = new Date();
            Timestamp timestamp = new Timestamp(d.getTime());
            Console.log(timestamp);
            String insertSQL = "insert into " + tableName +
                    "(" +
                    "`" + NAME + "`" +
                    ",`" + GMT_CREATE + "`" +
                    ") " +
                    "values(?,?)";
            PreparedStatement pst = manager.getPreparedStatement(insertSQL);
            pst.setString(1, "aa");
            pst.setTimestamp(2, timestamp);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
