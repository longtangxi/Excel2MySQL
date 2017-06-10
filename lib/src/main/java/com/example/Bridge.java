package com.example;

import com.example.table.Altitude;
import com.example.table.Project;
import com.xiaoleilu.hutool.lang.Console;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by ty on 2017/6/8.
 */

public class Bridge {


    public static void main(String[] args) {
        DBManager db = DBManager.getInstance();
        /*先完善bean的信息*/
        String querySQL = "select id,name from " + Project.tableName;
        PreparedStatement pstmt = db.getPreparedStatement(querySQL//传入控制结果集可滚动、可更新的参数
                , ResultSet.TYPE_SCROLL_INSENSITIVE
                , ResultSet.CONCUR_UPDATABLE);
        try {
            ResultSet rs = pstmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            for (int i = 1; i <= rowCount; i++) {
                rs.absolute(i);
                if ("杭深线".matches(".*" + rs.getString(2) + ".*")) {

                    Console.log(rs.getInt(1));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static DBManager mDBManager;

    public static void storeExcel2DB(SheetBean sheetBean) {
        DBManager db = DBManager.getInstance();
        setBean(sheetBean, db);//完善Bean类

        StringBuilder insertManySQL = new StringBuilder()
                .append("insert into " + Altitude.tableName)
                .append("(")
                .append("`" + Altitude.MILENUM + "`")
                .append(",`" + Altitude.MEASURE_TIME + "`")
                .append(",`" + Altitude.ALTITUDE + "`")
                .append(",`" + Altitude.GMT_CREATE + "`")
                .append(",`" + Altitude.GMT_MODIFIED + "`")
                .append(",`" + Altitude.LEVEL + "`")
                .append(",`" + Altitude.ADDR_WORK + "`")
                .append(",`" + Altitude.PROJECT_ID + "`")
                .append(",`" + Altitude.ADDR_DOT + "`")
                .append(",`" + Altitude.IS_INIT + "`")
                .append(") ")
                .append("values(?,?,?,?,?,?,?,?,?,?)");
        PreparedStatement pSmt = db.getPreparedStatement(insertManySQL.toString());
        if (pSmt == null) {
            try {
                throw new Exception("PreparedStatement为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (SheetBean.DotBean dotBean : sheetBean.getList()) {
            try {
                pSmt.setDouble(1, dotBean.getMilenum());
                pSmt.setTimestamp(2, new Timestamp(dotBean.getDate().getTime()));
                pSmt.setDouble(3, dotBean.getAltitude());
                Timestamp current = new Timestamp(new Date().getTime());
                pSmt.setTimestamp(4, current);
                pSmt.setTimestamp(5, current);
                pSmt.setInt(6, sheetBean.getLevel());
                pSmt.setString(7, sheetBean.getAddrWork());
                pSmt.setInt(8, sheetBean.getpID());
                pSmt.setString(9, sheetBean.getSheetName());
                pSmt.setInt(10, dotBean.isInitPoint() ? 1 : 0);
                pSmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据表格内容获取数据库表示
     *
     * @param bean
     * @param db
     */
    private static void setBean(SheetBean bean, DBManager db) {
    /*先完善bean的信息*/
        String querySQL = "select " + Project.ID + "," + Project.NAME + " from " + Project.tableName;//查询工程ID,名称
        PreparedStatement pstmt = db.getPreparedStatement(querySQL//传入控制结果集可滚动、可更新的参数
                , ResultSet.TYPE_SCROLL_INSENSITIVE
                , ResultSet.CONCUR_UPDATABLE);
        try {
            ResultSet rs = pstmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            for (int i = 1; i <= rowCount; i++) {
                rs.absolute(i);
                if (bean.getProjectName().matches(".*" + rs.getString(2) + ".*")) {//如果表格中的工程名称匹配数据库中的名称
                    bean.setpID(rs.getInt(1));//设置Bean中的ID
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
