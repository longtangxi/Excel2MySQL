package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by ty on 2017/6/6.
 */

public class DBManager {

    private Statement stmt = null;

    public DBManager() {
        connectDB();
    }

    private void connectDB() {
        Properties properties = new Properties();
        String propName = System.getProperty("user.dir") + "\\lib\\assets\\db.properties";

        try {
            properties.load(new FileInputStream(new File(propName)));
            String url = properties.getProperty("url");
            String database = properties.getProperty("database");
            String user = properties.getProperty("user");
            String passwd = properties.getProperty("passwd");
            Connection conn = DriverManager.getConnection(url+database, user, passwd);
            stmt = conn.createStatement();
//
//            ResultSet rs = stmt.executeQuery("select * from msg");
//            while (rs.next()) {
//                System.out.println(rs.getInt(1) + "\t" +
//                        rs.getString(2) + "\t" +
//                        rs.getString(3));
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Properties properties = new Properties();
        String propName = System.getProperty("user.dir") + "\\lib\\assets\\db.properties";

        try {
            properties.load(new FileInputStream(new File(propName)));
            String url = properties.getProperty("url");
            String database = properties.getProperty("database");
            String user = properties.getProperty("user");
            String passwd = properties.getProperty("passwd");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void executeSQL(String sql) throws Exception {
        if (null == stmt) {
            connectDB();
        }
        if (null == stmt) {
            throw new Exception("数据库连接异常");
        }
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
