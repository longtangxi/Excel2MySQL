package com.example;


import com.example.bean.AltitudeBean;
import com.example.bean.ConcernBean;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Console;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
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
            //工作表名
            sheetName = workbook.getSheetAt(i).getSheetName();
            if (sheetName.matches(Convert.strToUnicode("下行路堤纵断面"))
                    || sheetName.matches(Convert.strToUnicode("上行路堤纵断面"))
                    || sheetName.matches(Convert.strToUnicode("下行底座板纵断面"))
                    || sheetName.matches(Convert.strToUnicode("上行底座板纵断面"))
                    || sheetName.matches(Convert.strToUnicode("下行轨道板纵断面"))
                    || sheetName.matches(Convert.strToUnicode("上行轨道板纵断面"))) {
                sheet = workbook.getSheetAt(i);
                if (sheet != null) {
                    LinkedList<AltitudeBean> points = new LinkedList<>();
                    getDataFromExcel(sheet, concernBean, points);//从Excel表中获取想要的信息
                    Console.log("正在读取：" + sheetName);
                    if (points.size() > 0) {
                        Bridge.storeExcel2DB(concernBean, points);//获取成功，存数据库

                    }
                }
            }
        }


    }


    private static void getDataFromExcel(XSSFSheet sheet, ConcernBean concernBean, LinkedList<AltitudeBean> points) {
        TreeMap<Integer, Date> colDateMap = new TreeMap<>();//日期Map  <列号，日期>
        TreeMap<Integer, Date> altitudesMap = new TreeMap<>();//高程列号及对应的日期Map  <列号，日期>
        int colMilenum = -1;//里程号列 flag
        int colComment = -1;//备注列 flag
        Iterator<Row> iterator = sheet.iterator();//获取行的迭代器
        XSSFRow row;
        while (iterator.hasNext()) {
            row = (XSSFRow) iterator.next();//获得某行数据
            Iterator<Cell> colIt = row.cellIterator();

            Cell cell;
            double thisRowMilenum = -1f;//该行对应的里程号
            String thisRowDotnum = "";//该行对应的测点号
            String thisRowCPnum = "";//该行备注中对应的CP号
            boolean isInitPoint = false;//逐行读取，一行中只有一个初始点

            String regexIsBaseinfo = ".*" + Convert.strToUnicode("项目名称") + ".*" + Convert.strToUnicode("施工地点") + ".*" + Convert.strToUnicode("测量等级") + ".*";//*项目名称*施工地点*测量等级*
            String regexIsAltitude = ".*" + Convert.strToUnicode("高") + ".*" + Convert.strToUnicode("程") + ".*";//*高*程*
            String regexIsMilenum = ".*" + Convert.strToUnicode("里") + ".*" + Convert.strToUnicode("程") + ".*";//*里*程*
            String regexIsComment = ".*" + Convert.strToUnicode("对应CP") + ".*" + Convert.strToUnicode("点号") + ".*";//对应CP 点号
            String regexIsMilenumString = "^[k|K]\\d{1,4}\\+\\d{1,3}$";//*里程号的字符串形式

            while (colIt.hasNext()) {
                cell = colIt.next();
                switch (cell.getCellTypeEnum()) {
                    case STRING://字符串
                        String valueString = cell.getStringCellValue();//cell的值
                        int colString = cell.getColumnIndex(); //cell所处的列
                        /*获取数据集的基本信息*/
                        boolean isBaseinfo = valueString.matches(regexIsBaseinfo);
                        if (isBaseinfo) {
                            valueString = valueString.replaceAll("\\s+", "")
                                    .replaceAll("[\\uff1a|\\u003a]", "");//去除所有空格//去除中英字冒号
                            concernBean.setProjectName(getProjectName(valueString));//项目名称
                            long[] range = getProjectRange(valueString);//项目里程范围
                            concernBean.setStart(range[0]);
                            concernBean.setEnd(range[1]);

                            concernBean.setProjectType(getProjectType(valueString));//项目施工类型
                            concernBean.setAddress(getProjectAddr(valueString));//项目施工地点
                            concernBean.setLevel(getProjectLevel(valueString));//项目测量等级
                        }

                        boolean isAltitude = valueString.matches(regexIsAltitude);//*高*程* 必须转为unicode码，否则不会匹配成功
                        if (isAltitude) {//如果匹配"高程",说明该列下的数据为高程值
                            Iterator it = colDateMap.keySet().iterator();//获得存储好的日期所在列的集合
                            while (it.hasNext()) {
                                Integer dateCol = (Integer) it.next();
                                if ((colString - dateCol) <= 1) {//如果该Cell的列在某个日期所在列的右侧的合理范围内，则认为是该日期下所测的高程值
                                    altitudesMap.put(colString, colDateMap.get(dateCol));//将存放高程值的列号和对应的日期存储起来
                                    break;
                                }
                            }

                        }
                        boolean isMileNo = valueString.matches(regexIsMilenum);//*里*程* 如果该列对应的是里程号,记录下来
                        if (isMileNo && colMilenum == -1) {
                            colMilenum = colString;
                        }
                        if (colString == colMilenum && valueString.matches(regexIsMilenumString)) {//该列是里程号并且该单元格的值跟里程号的字符串形式匹配

                            String[] a = valueString.substring(1).split("\\+");
                            thisRowMilenum = Double.parseDouble(a[0]) * 1000 + Double.parseDouble(a[1]);
                            if (row.getCell(0).getCellTypeEnum() == CellType.STRING && thisRowDotnum.equals("")) {
                                thisRowDotnum = row.getCell(0).getStringCellValue();
                                Console.log("该行的点号是：" + thisRowDotnum);
                            }
                        }

                        boolean isComment = valueString.matches(regexIsComment);
                        if (isComment && colComment == -1) {
                            colComment = colString;
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
                            Console.log("该sheet中显示测量了：" + colDateMap.size() + "次");
                        }
                    /*获取高程值*/
                        //如果该列代表高程值&&且该行为有效数据行
                        if (altitudesMap.keySet().contains(colNumeric) && thisRowMilenum > 0) {
                            if (row.getCell(0).getCellTypeEnum() == CellType.NUMERIC && thisRowDotnum.equals("")) {
                                thisRowDotnum = (int) row.getCell(0).getNumericCellValue() + "";
//                                Console.log("该行的点号是：" + thisRowDotnum);
                            }
                            if (colComment != -1 && thisRowCPnum.equals("")) {//该列是里程号并且该单元格的值跟里程号的字符串形式匹配
                                thisRowCPnum = row.getCell(colComment).getStringCellValue();
//                                Console.log("该行的CP点号是：" + thisRowCPnum);
                            }
                            date = colDateMap.get(colNumeric);
                            AltitudeBean point = new AltitudeBean();
                            point.setGmtMeasure(date);
                            point.setMileNo(BigDecimal.valueOf(thisRowMilenum));
                            point.setHeight(BigDecimal.valueOf(valueNumeric));
                            point.setCpNo(thisRowCPnum);
                            point.setMeasureNo(thisRowDotnum);
                            point.setOnWhat(sheetName);
                            if (!isInitPoint) {
                                //如果该行之前的高程列还没有值，就就将其作为初始值（ps：后期还得改逻辑）
                                point.setBasepoint(true);
                                isInitPoint = true;
                            }
                            points.add(point);
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
