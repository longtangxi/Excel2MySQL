package com.example;


import com.example.table.Altitude;

import org.apache.poi.ss.usermodel.Cell;
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


public class ExcelManager {

    static XSSFRow row;

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

            AltitudeBean altitudeBean = new AltitudeBean();
            doSomething(sheet, altitudeBean);
        /*存数据库*/
            storeIntoDB(altitudeBean);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 将拼装好的数据插入数据库
     */
    private static void storeIntoDB(AltitudeBean bean) {
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
                "`" + Altitude.ALTITUDE + "`," +
                "`" + Altitude.CREATE_TIME + "`," +
                "`" + Altitude.UPDATE_TIME + "`" +
                ") " +
                "values(?,?,?,?,?)";
        PreparedStatement pSmt = null;
        try {

            pSmt = conn.prepareStatement(insertMany);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Iterator it = bean.getmData().keySet().iterator();
        while (it.hasNext()) {
            Date d = (Date) it.next();
            long measure_time = d.getTime() / 1000;//测量时间,java中生成的时间戳精确到毫秒级别，而unix中精确到秒级别
            TreeMap<Double, Double> data = bean.getmData().get(d);
            Iterator itt = data.keySet().iterator();
            while (itt.hasNext()) {
                Double milenum = (Double) itt.next();
                Double altitude = data.get(milenum);
                long curTime = System.currentTimeMillis() / 1000;
                try {
                    pSmt.setDouble(1, milenum);
                    pSmt.setLong(2, measure_time);
                    pSmt.setDouble(3, altitude);
                    pSmt.setLong(4, curTime);
                    pSmt.setLong(5, curTime);
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

    private static void doSomething(XSSFSheet sheet, AltitudeBean bean) {
        TreeMap<Integer, Date> colDateMap = new TreeMap<>();//日期Map  <列号，日期>
        TreeMap<Integer, Date> altitudesMap = new TreeMap<>();//高程列号及对应的日期Map  <列号，日期>
        int colMilenum = -1;
        Iterator<Row> iterator = sheet.iterator();//获取行的迭代器
        while (iterator.hasNext()) {

            row = (XSSFRow) iterator.next();//获得某行数据

            Iterator<Cell> cellIterator = row.cellIterator();

//            int rowNum = row.getRowNum();
//            System.out.println("row:" + rowNum);

//            int dot = -1;
            Cell cell;
//            if (cellIterator.hasNext()) {
//                cell = cellIterator.next();
//                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                    dot = (int) cell.getNumericCellValue();
//                }
//            }
            double thisRowMilenum = -1f;
            String regexIsBaseinfo = ".*(\\u9879\\u76ee\\u540d\\u79f0).*(\\u65bd\\u5de5\\u5730\\u70b9).*(\\u6d4b\\u91cf\\u7b49\\u7ea7).*";//*项目名称*施工地点*测量等级*
            String regexIsAltitude = ".*(\\u9ad8).*(\\u7a0b).*";//*高*程*
            String regexIsMilenum = ".*(\\u91cc).*(\\u7a0b).*";//*里*程*
            String regexIsMilenumString = "^[k|K]\\d{1,4}\\+\\d{1,3}$";//*里程号的字符串形势

            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                switch (cell.getCellTypeEnum()) {
                    case STRING://字符串
                        String valueString = cell.getStringCellValue();//cell的值
                        int colString = cell.getColumnIndex(); //cell所处的列
                        /*获取数据集的基本信息*/
                        boolean isBaseinfo = valueString.matches(regexIsBaseinfo);
                        if (isBaseinfo) {
                            int prjName = valueString.indexOf("项目名称");
                            int prjName1 = valueString.indexOf("\\u9879\\u76ee\\u540d\\u79f0");
                            int prjName2 = valueString.indexOf("\\u65bd\\u5de5\\u5730\\u70b9");
                            int prjName3 = valueString.indexOf("\\u6d4b\\u91cf\\u7b49\\u7ea7");
                            int prjName4 = valueString.indexOf("\\u65bd\\u5de5\\u5730\\u70b9");
                        }

                        boolean isAltitude = valueString.matches(regexIsAltitude);//*高*程* 必须转为unicode码，否则不会匹配成功
                        if (isAltitude) {//如果包含匹配"高程",说明该列下的数据为高程值
                            Iterator it = colDateMap.keySet().iterator();//获得存储好的日期所在列的集合
                            while (it.hasNext()) {
                                Integer dateCol = (Integer) it.next();
                                if ((colString - dateCol) <= 1) {//如果该Cell的列在某个日期所在列的右侧的合理范围内，则认为是该日期下所测的高程值
                                    altitudesMap.put(colString, colDateMap.get(dateCol));//将存放高程值的列号和对应的日期存储起来
                                    break;
                                }
                            }

                        }
                        boolean isMilenum = valueString.matches(regexIsMilenum);//*里*程* 如果该列对应的是里程号,记录下来
                        if (isMilenum && colMilenum == -1) {
                            colMilenum = colString;
                        }
                        if (colString == colMilenum && valueString.matches(regexIsMilenumString)) {//该列是里程号并且跟里程号的字符串形式匹配
//                            isValidRow = true;
                            String[] a = valueString.substring(1).split("\\+");
                            thisRowMilenum = Double.parseDouble(a[0]) * 1000 + Double.parseDouble(a[1]);
                        }
                        break;
                    case _NONE:
                        break;
                    case NUMERIC://数字
                        double valueNumeric = cell.getNumericCellValue();//cell的值
                        int colNumeric = cell.getColumnIndex();//cell所处的列

                        if (colNumeric == colMilenum && valueNumeric >= 0 && valueNumeric <= 1000 * 10000) {//如果该列属于里程号列并且里程号的数字形式
//                            isValidRow = true;
                            thisRowMilenum = valueNumeric;
                        }
                    /*获取日期*/
                        Date date = getDateFromCell(cell);
                        if (date != null) {
                            //该日期下的列号
                            colDateMap.put(colNumeric, date);
                        }
                    /*获取高程值*/
                        //如果该列代表高程值&&且该行为有效数据行
                        if (altitudesMap.keySet().contains(colNumeric) && thisRowMilenum > 0) {
                            date = colDateMap.get(colNumeric);
                            if (bean.getmData().get(date) == null) {
                                TreeMap<Double, Double> v = new TreeMap<>();
                                v.put(thisRowMilenum, valueNumeric);
                                bean.getmData().put(date, v);
                            } else {
                                bean.getmData().get(date).put(thisRowMilenum, valueNumeric);
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
