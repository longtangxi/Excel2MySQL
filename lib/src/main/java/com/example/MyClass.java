package com.example;


import com.example.table.Altitude;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;


public class MyClass {

    static XSSFRow row;

    //存储日期-点号-变形量
    static TreeMap<Date, TreeMap<Integer, Double>> mData = new TreeMap<>();
    private static DBManager mDBManager;


    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径
//        System.out.println(System.getProperty("file.encoding"));
        String filename = System.getProperty("user.dir") + "\\lib\\src\\main\\java\\com\\example\\a.xlsx";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filename));
            if (filename.endsWith(".xlsx")) {

            } else if (filename.endsWith(".xls")) {

            } else {
                throw new Exception("文件必须以xls或者xlsx结尾");
            }
            //构建一个高版本的EXCEL
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            //获取指定的sheet
            XSSFSheet sheet = (XSSFSheet) getSheet(workbook, "");
            if (sheet == null) {
                throw new Exception("没有找到指定的工作表");
            }
            doSomething(sheet);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        /*存数据库*/
        storeIntoDB();
    }

    private static void storeIntoDB() {
        Connection conn = initDB();

        try {
            mDBManager.executeUpdate(Altitude.createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String insertMany = "insert into " + Altitude.tableName +
                "(" +
                "`" + Altitude.DOT_NUM + "`," +
                "`" + Altitude.MEASURE_TIME + "`," +
                "`" + Altitude.ALTITUDE + "`" +
                ") " +
                "values(?,?,?)";
        PreparedStatement pSmt = null;
        try {

            pSmt = conn.prepareStatement(insertMany);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Iterator it = mData.keySet().iterator();
        while (it.hasNext()) {
            Date d = (Date) it.next();
            long measure_time = d.getTime()/1000;//测量时间,java中生成的时间戳精确到毫秒级别，而unix中精确到秒级别
            TreeMap<Integer, Double> data = mData.get(d);
            Iterator itt = data.keySet().iterator();
            while (itt.hasNext()) {
                Integer dotNum = (Integer) itt.next();
                Double altitude = data.get(dotNum);
                try {
                    pSmt.setInt(1, dotNum);
                    pSmt.setLong(2, measure_time);
                    pSmt.setDouble(3, altitude);
                    pSmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static Connection initDB() {
        mDBManager = new DBManager();
//        String creDB = "CREATE DATABASE IF NOT EXISTS st_work";//创建一个数据库
//            mDBManager.executeUpdate(creDB);
        Connection conn = mDBManager.connectDB();
        if (conn == null) {
            try {
                throw new Exception("Connection不能爲空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    /**
     * 根据指定规则得到一个工作表sheet
     *
     * @param workbook
     * @param name
     * @return
     */
    private static Sheet getSheet(Workbook workbook, String name) {
        int shtNum = workbook.getNumberOfSheets();
        for (int i = 0; i < shtNum; i++) {
            String shtName = workbook.getSheetAt(i).getSheetName();//工作表名
            if (shtName.matches("\\u4e0b\\u884c\\u8def\\u5824\\u7eb5\\u65ad\\u9762")) {//下行路堤纵断面
                return workbook.getSheetAt(i);
            }
        }
        return null;
    }

    private static void doSomething(XSSFSheet sheet) {
        TreeMap<Integer, Date> colDateMap = new TreeMap<>();//日期Map  <列号，日期>
        TreeMap<Integer, Date> altitudesMap = new TreeMap<>();//高程列号及对应的日期Map  <列号，日期>
        Iterator<Row> iterator = sheet.iterator();//获取行的迭代器
        while (iterator.hasNext()) {

            row = (XSSFRow) iterator.next();//获得某行数据

            Iterator<Cell> cellIterator = row.cellIterator();

//            int rowNum = row.getRowNum();
//            System.out.println("row:" + rowNum);

            int dot = -1;
            Cell cell;
            if (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    dot = (int) cell.getNumericCellValue();
                }
            }
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                switch (cell.getCellTypeEnum()) {
                    case STRING://字符串
                        String value = cell.getStringCellValue();
                        boolean isAltitude = value.matches(".*(\\u9ad8).*(\\u7a0b).*");//*高*程* 必须转为unicode码，否则不会匹配成功
                        if (isAltitude) {//如果包含匹配"高程",说明该列下的数据为高程值
                            Iterator it = colDateMap.keySet().iterator();//获得存储好的日期所在列的集合
                            while (it.hasNext()) {
                                Integer dateCol = (Integer) it.next();
                                if ((cell.getColumnIndex() - dateCol) <= 1) {//如果该Cell的列在某个日期所在列的右侧的合理范围内，则认为是该日期下所测的高程值
                                    altitudesMap.put(cell.getColumnIndex(), colDateMap.get(dateCol));//将存放高程值的列号和对应的日期存储起来
                                    break;
                                }
                            }

                        }

                        break;
                    case _NONE:
                        break;
                    case NUMERIC://数字
                    /*获取点号*/
                    /*获取日期*/
                        Date date = getDateFromCell(cell);
                        if (date != null) {
                            //该日期下的列号
                            int column = cell.getColumnIndex();
                            colDateMap.put(column, date);
                        }
                    /*获取高程值*/
                        int columnIndex = cell.getColumnIndex();
                        //如果该列代表高程值&&点号不是-1，说明该行的第一个值为点号
                        if (altitudesMap.keySet().contains(columnIndex) && dot != -1) {
                            double altitude = cell.getNumericCellValue();
                            date = colDateMap.get(columnIndex);
                            if (mData.get(date) == null) {

                                TreeMap<Integer, Double> v = new TreeMap<>();
                                v.put(dot, altitude);
                                mData.put(date, v);
                            } else {
                                mData.get(date).put(dot, altitude);
                            }
                        }
                        break;
                    case FORMULA://公式
//                    keepDateDotData(sheet, cell);
                        break;
                    case BLANK:
                        break;
                    case BOOLEAN:
                        break;
                    case ERROR:
                        break;
                }
            }
            dot = -1;
        }

    }


    /**
     * 根据Cell返回的DateFormat值判定是否为日期
     *
     * @param cell
     * @return 如果不是日期格式，返回null
     * 如果是，则返回正确的日期格式
     */
    private static Date getDateFromCell(Cell cell) {
        short format = cell.getCellStyle().getDataFormat();
        //只适用于Excel自带的日期格式（前两种1.2012/1/13  2.2012年1月13日）
        if (DateUtil.isCellDateFormatted(cell) ||
                format == 57 ||
                format == 58 ||
                format == 31) {

            double value = cell.getNumericCellValue();
            Date date = DateUtil.getJavaDate(value);
//            System.out.println(sdf.format(date));
            return date;
        }
        return null;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
