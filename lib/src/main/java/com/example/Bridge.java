package com.example;

import com.example.bean.AltitudeBean;
import com.example.bean.ConcernBean;
import com.xiaoleilu.hutool.lang.Console;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ty on 2017/6/8.
 */

public class Bridge {


    public static void main(String[] args) {

        Dao dao = DaoUtil.getDao();
        dao.create(ConcernBean.class, false);


//        DBManager db = DBManager.getInstance();
//        /*先完善bean的信息*/
//        String querySQL = "select id,name from " + Project.tableName;
//        PreparedStatement pstmt = db.getPreparedStatement(querySQL//传入控制结果集可滚动、可更新的参数
//                , ResultSet.TYPE_SCROLL_INSENSITIVE
//                , ResultSet.CONCUR_UPDATABLE);
//        try {
//            ResultSet rs = pstmt.executeQuery();
//            rs.last();
//            int rowCount = rs.getRow();
//            for (int i = 1; i <= rowCount; i++) {
//                rs.absolute(i);
//                if ("杭深线".matches(".*" + rs.getString(2) + ".*")) {
//
//                    Console.log(rs.getInt(1));
//                    break;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    private static DBManager mDBManager;

    public static void storeExcel2DB(ConcernBean concernBean, LinkedList<AltitudeBean> points) {
        Console.log(concernBean.toString());
        Dao dao = DaoUtil.getDao();
        dao.create(ConcernBean.class, false);
        List<ConcernBean> concernBeans = dao.query(ConcernBean.class
                , Cnd.where("start", "=", concernBean.getStart()));
        if (concernBeans.size() > 0) {
            Console.log(concernBeans.get(0).getId());
        }

        Console.log("该表共有：" + points.size() + " 个点");
//        DBManager db = DBManager.getInstance();
//
//        try {
//            DaoUp.me().init(".\\lib\\assets\\db.properties");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Dao dao = DaoUp.me().dao();
//        setBean(sheetBean, db);//完善Bean类
//
//        StringBuilder insertManySQL = new StringBuilder()
//                .append("insert into " + Altitude.tableName)
//                .append("(")
//                .append("`" + Altitude.MILENUM + "`")
//                .append(",`" + Altitude.MEASURE_TIME + "`")
//                .append(",`" + Altitude.ALTITUDE + "`")
//                .append(",`" + Altitude.GMT_CREATE + "`")
//                .append(",`" + Altitude.GMT_MODIFIED + "`")
//                .append(",`" + Altitude.LEVEL + "`")
//                .append(",`" + Altitude.ADDR_WORK + "`")
//                .append(",`" + Altitude.PROJECT_ID + "`")
//                .append(",`" + Altitude.ADDR_DOT + "`")
//                .append(",`" + Altitude.IS_INIT + "`")
//                .append(",`" + Altitude.FOCUS_ID + "`")
//                .append(",`" + Altitude.CPNUM + "`")
//                .append(",`" + Altitude.MEASURENUM + "`")
//                .append(") ")
//                .append("values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
//        PreparedStatement pSmt = db.getPreparedStatement(insertManySQL.toString());
//        if (pSmt == null) {
//            try {
//                throw new Exception("PreparedStatement为空");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (SheetBean.DotBean dotBean : sheetBean.getList()) {
//            try {
//                pSmt.setBigDecimal(1, dotBean.getMilenum());
//                pSmt.setTimestamp(2, new Timestamp(dotBean.getDate().getTime()));
//                pSmt.setBigDecimal(3, dotBean.getAltitude());
//                Timestamp current = new Timestamp(new Date().getTime());
//                pSmt.setTimestamp(4, current);
//                pSmt.setTimestamp(5, current);
//                pSmt.setInt(6, sheetBean.getLevel());
//                pSmt.setString(7, sheetBean.getAddrWork());
//                pSmt.setInt(8, sheetBean.getpID());
//                pSmt.setString(9, sheetBean.getSheetName());
//                pSmt.setInt(10, dotBean.isInitPoint() ? 1 : 0);
//                pSmt.setInt(11, sheetBean.getFoucusID());
//                pSmt.setString(12, dotBean.getCp_num());
//                pSmt.setString(13, dotBean.getMeasure_num());
//                pSmt.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }

    /**
     * 根据表格内容获取数据库表示
     *
     * @param db
     */
    private static void setBean(DBManager db) {
    /*先完善bean的信息*/
//        StringBuilder sql = new StringBuilder()
//                .append("select ")
//                .append(Project.ID)
//                .append("," + Project.NAME)
//                .append(" from ")
//                .append(Project.tableName);//查询工程ID,名称
//        PreparedStatement pstmt = db.getPreparedStatement(sql.toString() //传入控制结果集可滚动、可更新的参数
//                , ResultSet.TYPE_SCROLL_INSENSITIVE
//                , ResultSet.CONCUR_UPDATABLE);
//        try {
//            ResultSet rs = pstmt.executeQuery();
//            rs.last();
//            int rowCount = rs.getRow();
//            for (int i = 1; i <= rowCount; i++) {
//                rs.absolute(i);
//                if (bean.getProjectName().matches(".*" + rs.getString(2) + ".*")) {//如果表格中的工程名称匹配数据库中的名称
//                    bean.setpID(rs.getInt(1));//设置Bean中的ID
//                    break;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        int direction = bean.getProjectType().matches(".*" + Convert.strToUnicode("沉降") + ".*") ? 1 : 0;
//        sql = new StringBuilder()
//                .append("select ")
//                .append(Focus.ID)
//                .append(" from ")
//                .append(Focus.tableName)
//                .append(" where ")
//                .append(Focus.NAME + " like 'K646+%'")
//                .append(" and " + Focus.CONTENT + "=" + direction);
////        Console.log(sql.toString());
//        pstmt = db.getPreparedStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE
//                , ResultSet.CONCUR_UPDATABLE);
//        try {
//            ResultSet rs = pstmt.executeQuery();
//            rs.last();
//            int rowCount = rs.getRow();
//            for (int i = 1; i <= rowCount; i++) {
//                rs.absolute(i);
//                bean.setFoucusID(rs.getInt(1));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

}
