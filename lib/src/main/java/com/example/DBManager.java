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


    public Connection connectDB() {
        Properties properties = new Properties();
        String propName = System.getProperty("user.dir") + "\\lib\\assets\\db.properties";

        try {
            properties.load(new FileInputStream(new File(propName)));
            String url = properties.getProperty("url");
            String database = properties.getProperty("database");
            String user = properties.getProperty("user");
            String passwd = properties.getProperty("passwd");
            Connection conn = DriverManager.getConnection(url+database, user, passwd);
            return conn;
//            stmt = conn.createStatement();
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
        return null;
    }




    public void executeUpdate(String sql) throws Exception {
        Connection conn  = connectDB();
        if (null == conn){
            throw new Exception("数据库连接异常，没有获得Connection对象");
        }
        Statement stmt = conn.createStatement();
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



//    public void insertMany(String sql) throws Exception {
//        Connection conn  = connectDB();
//        if (null == conn){
//            throw new Exception("数据库连接异常，没有获得Connection对象");
//        }
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        try {
//            pstmt.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
