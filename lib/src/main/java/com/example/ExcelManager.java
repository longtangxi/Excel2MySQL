package com.example;


import com.example.bean.AltitudeBean;
import com.example.bean.ConcernBean;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Console;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExcelManager {

    private static String sheetName;


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
            XSSFWorkbook workbook = new XSSFWorkbook(fis);//构建一个高版本的EXCEL

            formatData(workbook);//开始格式化Excel数据
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private static void formatData(XSSFWorkbook workbook) {
        XSSFSheet sheet = null;
        int shtNum = workbook.getNumberOfSheets();
        ConcernBean concernBean = new ConcernBean();//重点段实体类

        for (int i = 0; i < shtNum; i++) {
            sheetName = workbook.getSheetAt(i).getSheetName();//从观测成果sheet中获取基本信息
            if (sheetName.matches(".*" + Convert.strToUnicode("观测成果") + "$")) {
                sheet = workbook.getSheetAt(i);
                if (sheet != null) {
                    Console.log("正在读取：" + sheetName);
                    getInfoFromExcel(sheet, concernBean);//从Excel表中获取想要的信息
                }
            }
        }

        for (int i = 0; i < shtNum; i++) {
            //工作表名
            sheetName = workbook.getSheetAt(i).getSheetName();//从纵断面sheet获取数据
            if (sheetName.matches("^" + Convert.strToUnicode("下行路堤纵断面") + "$")
                    || sheetName.matches("^" + Convert.strToUnicode("上行路堤纵断面") + "$")
                    || sheetName.matches("^" + Convert.strToUnicode("下行底座板纵断面") + "$")
                    || sheetName.matches("^" + Convert.strToUnicode("上行底座板纵断面") + "$")
                    || sheetName.matches("^" + Convert.strToUnicode("下行轨道板纵断面") + "$")
                    || sheetName.matches("^" + Convert.strToUnicode("上行轨道板纵断面") + "$")) {
                sheet = workbook.getSheetAt(i);
                if (sheet != null) {
                    Console.log("正在读取：" + sheetName);
                    LinkedList<AltitudeBean> points = new LinkedList<>();
                    getDataFromExcel(sheet, points);//从Excel表中获取想要的信息
                    if (points.size() > 0) {
                        Console.log("concernBean:" + concernBean.toString());
                        Console.log("points:" + points.size());

                        Bridge.storeExcel2DB(concernBean, points);//获取成功，存数据库
                    }
                }
            }
        }

    }

    private static void getInfoFromExcel(XSSFSheet sheet, ConcernBean concernBean) {
        Iterator<Row> rowIt = sheet.iterator();//获取行的迭代器
        XSSFRow row;
        while (rowIt.hasNext()) {
            row = (XSSFRow) rowIt.next();//获得某行数据
            Iterator<Cell> colIt = row.cellIterator();
            Cell cell;
            while (colIt.hasNext()) {
                cell = colIt.next();
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    String valStr = cell.getStringCellValue();
                    if (valStr.matches(".*" + Convert.strToUnicode("观测成果"))) {
                        concernBean.setName(valStr);//项目名称
                        String regex = "[K|k].*\\d{1,3}";//

                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(valStr);
                        while (m.find()) {
                            String mile = valStr.substring(m.start(), m.end());//K647+050-K647+350
                            mile = mile.replaceAll("[k|K]", "");//647+050-647+350
                            String[] startAndEnd = mile.split("[~|-]");//[647+050,647+350]
                            if (startAndEnd.length == 2) {
                                String[] startStr = startAndEnd[0].split("\\+");
                                long startLong = Long.parseLong(startStr[0]) * 1000 + Long.parseLong(startStr[1]);
                                concernBean.setStart(startLong);
                                String[] endStr = startAndEnd[1].split("\\+");
                                long endLong = Long.parseLong(endStr[0]) * 1000 + Long.parseLong(endStr[1]);
                                concernBean.setEnd(endLong);
                            }
                        }
                    }
                }
//                        if (valStr.matches(".*" + Convert.strToUnicode("总第") + ".*" + Convert.strToUnicode("期"))) {
//
//                        }
//
//                        valStr = valStr.replaceAll("\\s+", "")
//                                .replaceAll("[\\uff1a|\\u003a]", "");//去除所有空格//去除中英字冒号
//                        concernBean.setProjectName(getProjectName(valStr));//项目名称
//                        long[] range = getProjectRange(valStr);//项目里程范围
//                        concernBean.setStart(range[0]);
//                        concernBean.setEnd(range[1]);
//
//                        concernBean.setProjectType(getProjectType(valStr));//项目施工类型
//                        concernBean.setAddress(getProjectAddr(valStr));//项目施工地点
//                        concernBean.setLevel(getProjectLevel(valStr));//项目测量等级

            }
        }

    }


    private static void getDataFromExcel(XSSFSheet sheet, LinkedList<AltitudeBean> points) {

        TreeMap<Integer, Date> dates = new TreeMap<>();//日期列
        TreeMap<Integer, Integer> srcTimes = new TreeMap<>();//期数列,TreeMap默认升序
        TreeMap<Integer, Integer> dstTimes = new TreeMap<>();//期数列,TreeMap默认升序
        LinkedHashMap<Integer, Date> heights = new LinkedHashMap<>();//高程列
        //
        int measurePointColumn = -1;//测点列
        int mileNoColumn = -1;//里程列
        int cpNoColumn = -1;//cp点号列

        Iterator<Row> iterator = sheet.iterator();//获取行的迭代器
        XSSFRow row;
        while (iterator.hasNext()) {
            row = (XSSFRow) iterator.next();//获得某行数据
            Iterator<Cell> colIt = row.cellIterator();

            BigDecimal mileNo = null;//该行对应的里程号
            String measureNo = null;//该行对应的测点号
            String cpNo = null;//该行对应的CP号


            Cell cell;
            boolean isInitPoint = false;//逐行读取，一行中只有一个初始点

            while (colIt.hasNext()) {
                cell = colIt.next();
                //日期
                //期数
                //高程
                /*----------日期----------*/
                int column = cell.getColumnIndex();
                Date d = getDateFromCell(cell);
                if (d != null) {
                    dates.put(column, d);
                }
                /*----------日期----------*/

                /*----------期数----------*/
                int time = getTimesFromCell(cell);//返回表格中实际显示的期数，初测返回0
                if (time != -1) {
                    if (srcTimes.values().contains(time) || time == 0 || time == 1) {
                        srcTimes.put(column, ++time);
                    } else {
                        srcTimes.put(column, time);
                    }

                }
                /*----------期数----------*/

                /*----------高程----------*/
                boolean isHeigtColumn = isHeightCoumn(cell);
                if (isHeigtColumn) {
                    Iterator it = dates.keySet().iterator();
                    int leftColumn = -1;
                    while (it.hasNext()) {
                        int rightColumn = (int) it.next();
                        if (column < rightColumn) {
                            heights.put(column, dates.get(leftColumn));//高程列及其对应的日期
                            break;
                        } else if (!it.hasNext()) {
                            //到达最后一列
                            heights.put(column, dates.get(rightColumn));//最后一列，没有下一列与之对比
                            break;
                        } else {
                            //左闭右开，如果不在该区间
                            leftColumn = rightColumn;
                        }


                    }
                    leftColumn = -1;
                    it = srcTimes.keySet().iterator();
                    while (it.hasNext()) {
                        int rightColumn = (int) it.next();
                        if (column < rightColumn) {
                            dstTimes.put(column, srcTimes.get(leftColumn));//高程列及其对应的测量期数
                            break;
                        } else if (!it.hasNext()) {
                            //到达最后一列
                            dstTimes.put(column, srcTimes.get(rightColumn));//最后一列，没有下一列与之对比
                            break;
                        } else {
                            //左闭右开，如果不在该区间
                            leftColumn = rightColumn;
                        }


                    }
                }
                /*----------高程----------*/

                /*----------测点----------*/
                boolean isMeasurePointColumn = isMeasurePointColumn(cell);
                if (isMeasurePointColumn) {
                    measurePointColumn = column;
                }
                /*----------测点----------*/

                /*----------里程----------*/
                boolean isMileNoColumn = isMileNoColumn(cell);
                if (isMileNoColumn) {
                    mileNoColumn = column;
                }
                /*----------里程----------*/

                /*----------CP点----------*/
                boolean isCpNoColumn = isCpNoColumn(cell);
                if (isCpNoColumn) {
                    cpNoColumn = column;
                }
                /*----------CP点----------*/

                /*----------以里程号作为判断有效行的依据----------*/

                if (mileNoColumn == column && mileNo == null) {
                    mileNo = getMileNo(cell);
                }
                if (mileNo != null && (measureNo == null || cpNo == null)) {
                    measureNo = getMeasureNoFromCell(row.getCell(measurePointColumn));
                    cpNo = getCPNoFromCell(row.getCell(cpNoColumn));
                }

                if (mileNo != null && heights.keySet().contains(column)) {
                    double val = cell.getCellTypeEnum() == CellType.NUMERIC ? cell.getNumericCellValue() : -1;
                    if (val > 0) {
                        BigDecimal height = BigDecimal.valueOf(val);
                        AltitudeBean point = new AltitudeBean();
                        point.setMeasureNo(measureNo);
                        point.setMileNo(mileNo);
                        point.setHeight(height);
                        point.setGmtMeasure(heights.get(column));
                        point.setTimes(dstTimes.get(column));
                        point.setOnWhat(sheetName);
                        point.setCpNo(cpNo);
                        if (!isInitPoint) {
                            //如果该行之前的高程列还没有值，就就将其作为初始值（ps：后期还得改逻辑）
                            point.setBasepoint(true);
                            isInitPoint = true;
                        }
                        points.add(point);
                    }
                }

            }
        }
        Console.log(srcTimes);
        Console.log(heights);


    }

    private static String getCPNoFromCell(XSSFCell cell) {
        if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return cell.getNumericCellValue() + "";
        }
        return "";
    }

    private static String getMeasureNoFromCell(XSSFCell cell) {
        if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return cell.getNumericCellValue() + "";
        }
        return "";
    }

    private static BigDecimal getMileNo(Cell cell) {
        if (cell.getCellTypeEnum() == CellType.STRING) {
            String regex = "^[k|K]\\d{1,4}\\+\\d{1,3}";//*里程号的字符串形式K646+921、K0181+157.8
            String val = cell.getStringCellValue();
            if (val.matches(regex)) {
                String[] a = val.replaceAll("[k|K]", "").split("\\+");
                Double res = Double.parseDouble(a[0]) * 1000 + Double.parseDouble(a[1]);
                return BigDecimal.valueOf(res);
            }
            return null;
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            double val = cell.getNumericCellValue();
            if (val >= 0 && val <= 9999999) {
                return BigDecimal.valueOf(val);
            }
            return null;
        }
        return null;
    }

    private static boolean isCpNoColumn(Cell cell) {
        if (cell.getCellTypeEnum() != CellType.STRING) {
            return false;
        }
        String val = cell.getStringCellValue();
        if (val.matches(".*" + Convert.strToUnicode("对应CP") + ".*" + Convert.strToUnicode("点号") + ".*")) {
            return true;
        }
        return false;
    }

    private static boolean isMileNoColumn(Cell cell) {
        if (cell.getCellTypeEnum() != CellType.STRING) {
            return false;
        }
        String val = cell.getStringCellValue();
        if (val.matches(".*" + Convert.strToUnicode("里") + ".*" + Convert.strToUnicode("程") + ".*")) {
            return true;
        }
        return false;
    }


    private static boolean isMeasurePointColumn(Cell cell) {
        if (cell.getCellTypeEnum() != CellType.STRING) {
            return false;
        }
        String val = cell.getStringCellValue();
        if (val.matches(".*" + Convert.strToUnicode("测") + ".*" + Convert.strToUnicode("点") + ".*")) {
            return true;
        }
        return false;

    }

    private static boolean isHeightCoumn(Cell cell) {

        if (cell.getCellTypeEnum() != CellType.STRING) {
            return false;
        }

        String val = cell.getStringCellValue();
        if (val.matches(".*" + Convert.strToUnicode("高") + ".*" + Convert.strToUnicode("程") + ".*")) {
            return true;
        }
        return false;
    }

    private static int getTimesFromCell(Cell cell) {

        if (cell.getCellTypeEnum() != CellType.STRING) {
            return -1;
        }

        String val = cell.getStringCellValue();
        if (val.matches(".*" + Convert.strToUnicode("初") + ".*" + Convert.strToUnicode("测") + ".*")) {
            return 0;
        }
        if (val.matches(".*" + Convert.strToUnicode("第") + ".*\\d+" + Convert.strToUnicode("次") + ".*")) {

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(val);
            if (m.find()) {
                return Integer.parseInt(val.substring(m.start(), m.end()));
            }
        }

        return -1;
    }

    /**
     * 获取项目测量等级
     *
     * @param valueString
     * @return
     */
    private static int getProjectLevel(String valueString) {
        String plevel = "测量等级";
        String regex = Convert.strToUnicode(plevel) + "[\\u0391-\\uFFE5]+" + Convert.strToUnicode("等");

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(valueString);
        while (m.find()) {
            String res = valueString.substring(m.start() + plevel.length(), m.end());
            if (res.indexOf("一") != -1) {
                return 1;
            } else if (res.indexOf("二") != -1) {
                return 2;
            } else if (res.indexOf("三") != -1) {
                return 3;
            } else if (res.indexOf("四") != -1) {
                return 4;
            }
        }
        return -1;
    }

    /**
     * 获取项目测量位置
     *
     * @param valueString
     * @return
     */
    private static String getProjectAddr(String valueString) {
        String paddr = "施工地点";

        String plevel = "测量等级";
        String regex = Convert.strToUnicode(paddr) + "[\\u0391-\\uFFE5]+" + Convert.strToUnicode(plevel);

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(valueString);
        while (m.find()) {
            return valueString.substring(m.start() + paddr.length(), m.end() - plevel.length());
        }
        return "";
    }

    /**
     * 获取项目施工类型
     *
     * @param valueString
     * @return
     */
    private static String getProjectType(String valueString) {
        String ptype = "项目类型";
        String paddr = "施工地点";
        String regex = "\\d{1}[\\u0391-\\uFFE5]+" + Convert.strToUnicode(paddr);//

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(valueString);
        while (m.find()) {
            return valueString.substring(m.start() + 1, m.end() - paddr.length());
        }
        return "";
    }

    /**
     * 获取项目里程范围
     *
     * @param valueString
     */
    private static long[] getProjectRange(String valueString) {
        String regex = "[k|K].*\\+\\d{1,3}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(valueString);
        while (m.find()) {
            String[] lowAndHigh = valueString.substring(m.start(), m.end()).replaceAll("[k|K]", "").split("[～|~]");
            if (lowAndHigh.length == 2) {
                String[] t = lowAndHigh[0].split("\\+");
                long[] result = new long[2];
                if (t.length == 2) {
                    long lower = Long.parseLong(t[0]) * 1000 + Long.parseLong(t[1]);//小里程
                    result[0] = lower;
                }
                t = lowAndHigh[1].split("\\+");
                if (t.length == 2) {
                    long higher = Long.parseLong(t[0]) * 1000 + Long.parseLong(t[1]);//大里程
                    result[1] = higher;
                }
                return result;
            }
        }
        return new long[2];
    }

    /**
     * 获取项目名称
     *
     * @param valueString
     * @return
     */
    private static String getProjectName(String valueString) {
        String pname = "项目名称";
        String regex = Convert.strToUnicode(pname) + "[\\u0391-\\uFFE5]+" + Convert.strToUnicode("线");//
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(valueString);
        while (m.find()) {
            return valueString.substring(m.start() + pname.length(), m.end());
        }
        return "";
    }


    /**
     * 根据Cell返回的DateFormat值判定是否为日期
     *
     * @param cell
     * @return 如果不是日期格式，返回null
     * 如果是，则返回正确的日期格式
     */
    private static Date getDateFromCell(Cell cell) {
        if (cell.getCellTypeEnum() != CellType.NUMERIC) {
            return null;
        }
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
