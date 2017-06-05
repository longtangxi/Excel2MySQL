package com.example;


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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;


public class MyClass {

    static XSSFRow row;
    //���ڸ�ʽ������
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //�洢����-���-������
    static TreeMap<Date, TreeMap<Integer, Double>> mData = new TreeMap<>();


    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));//user.dirָ���˵�ǰ��·��
        String filename = System.getProperty("user.dir") + "\\lib\\src\\main\\java\\com\\example\\a.xlsx";
        System.out.println(System.getProperty("file.encoding"));

//        System.out.println(filePath.endsWith(".xls"));
//        System.exit(0);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filename));
            if (filename.endsWith(".xlsx")) {

            } else if (filename.endsWith(".xls")) {

            } else {
                throw new Exception("�ļ�������xls����xlsx��β");
            }
            //����һ���߰汾��EXCEL
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            //��ȡָ����sheet
            XSSFSheet sheet = (XSSFSheet) getSheet(workbook, "");
            if (sheet == null) {
                throw new Exception("û���ҵ�ָ���Ĺ�����");
            }
            doSomething(sheet);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Iterator it = mData.keySet().iterator();
        while (it.hasNext()) {
            Date date = (Date) it.next();
            TreeMap<Integer, Double> data = mData.get(date);
            System.out.println(sdf.format(date) + "  " + data + "  size:" + data.size());
//            while (itt.hasNext()) {
//                Integer dott = (Integer) itt.next();
//                System.out.println("����:" + sdf.format(date) + "  ���:" + dott + "  �߳�:");
//            }
        }
    }

    /**
     * ����ָ������õ�һ��������sheet
     *
     * @param workbook
     * @param name
     * @return
     */
    private static Sheet getSheet(Workbook workbook, String name) {
        int shtNum = workbook.getNumberOfSheets();
        for (int i = 0; i < shtNum; i++) {
            String shtName = workbook.getSheetAt(i).getSheetName();//��������
            if (shtName.matches("\\u4e0b\\u884c\\u8def\\u5824\\u7eb5\\u65ad\\u9762")) {//����·���ݶ���
                return workbook.getSheetAt(i);
            }
        }
        return null;
    }

    private static void doSomething(XSSFSheet sheet) {
        TreeMap<Integer, Date> colDateMap = new TreeMap<>();//����Map  <�кţ�����>
        TreeMap<Integer, Date> altitudesMap = new TreeMap<>();//�߳��кż���Ӧ������Map  <�кţ�����>
        Iterator<Row> iterator = sheet.iterator();//��ȡ�еĵ�����
        while (iterator.hasNext()) {

            row = (XSSFRow) iterator.next();//���ĳ������

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
                    case STRING://�ַ���
                        String value = cell.getStringCellValue();
                        boolean isAltitude = value.matches(".*(\\u9ad8).*(\\u7a0b).*");//*��*��* ����תΪunicode�룬���򲻻�ƥ��ɹ�
                        if (isAltitude) {//�������ƥ��"�߳�",˵�������µ�����Ϊ�߳�ֵ
                            Iterator it = colDateMap.keySet().iterator();//��ô洢�õ����������еļ���
                            while (it.hasNext()) {
                                Integer dateCol = (Integer) it.next();
                                if ((cell.getColumnIndex() - dateCol) <= 1) {//�����Cell������ĳ�����������е��Ҳ�ĺ���Χ�ڣ�����Ϊ�Ǹ�����������ĸ߳�ֵ
                                    altitudesMap.put(cell.getColumnIndex(), colDateMap.get(dateCol));//����Ÿ߳�ֵ���кźͶ�Ӧ�����ڴ洢����
                                    break;
                                }
                            }

                        }

                        break;
                    case _NONE:
                        break;
                    case NUMERIC://����
                    /*��ȡ���*/
                    /*��ȡ����*/
                        Date date = getDateFromCell(cell);
                        if (date != null) {
                            //�������µ��к�
                            int column = cell.getColumnIndex();
                            colDateMap.put(column, date);
                        }
                    /*��ȡ�߳�ֵ*/
                        int columnIndex = cell.getColumnIndex();
                        //������д���߳�ֵ&&��Ų���-1��˵�����еĵ�һ��ֵΪ���
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
                    case FORMULA://��ʽ
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
     * ����Cell���ص�DateFormatֵ�ж��Ƿ�Ϊ����
     *
     * @param cell
     * @return ����������ڸ�ʽ������null
     * ����ǣ��򷵻���ȷ�����ڸ�ʽ
     */
    private static Date getDateFromCell(Cell cell) {
        short format = cell.getCellStyle().getDataFormat();
        //ֻ������Excel�Դ������ڸ�ʽ��ǰ����1.2012/1/13  2.2012��1��13�գ�
        if (DateUtil.isCellDateFormatted(cell) ||
                format == 57 ||
                format == 58 ||
                format == 31) {

            double value = cell.getNumericCellValue();
            Date date = DateUtil.getJavaDate(value);
            System.out.println(sdf.format(date));
            return date;
        }
        return null;
    }

    /**
     * ������������֮����������
     *
     * @param smdate ��С��ʱ��
     * @param bdate  �ϴ��ʱ��
     * @return �������
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
