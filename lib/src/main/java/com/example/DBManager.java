package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by ty on 2017/6/6.
 */

public class DBManager {
    private static DBManager dbManager = null;
    private Connection conn = null;

    private DBManager() {
        conn = connectDB();
        if (null == conn) {
            try {
                throw new Exception("数据库连接异常，Connection对象为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static DBManager getInstance() {
        if (dbManager == null)
            return new DBManager();
        return dbManager;
    }


    public Connection connectDB() {
        Properties properties = new Properties();
        String propName = System.getProperty("user.dir") + "\\lib\\assets\\db.properties";

        try {
            /*读取配置文件*/
            properties.load(new FileInputStream(new File(propName)));
            String url = properties.getProperty("url");
            String database = properties.getProperty("database");
            String user = properties.getProperty("user");
            String passwd = properties.getProperty("passwd");
            Connection conn = DriverManager.getConnection(url + database, user, passwd);
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Statement getStatement() {
        try {
            Statement stmt = conn.createStatement();
            return stmt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement getPreparedStatement(String sql) {

        try {
            PreparedStatement p = conn.prepareStatement(sql);
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement getPreparedStatement(String sql, int resultSetType,
                                                  int resultSetConcurrency) {

        try {
            PreparedStatement p = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String sql) throws Exception {
        Statement stmt = getStatement();
        if (stmt == null) {
            throw new Exception("Statement为空");
        }
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
