package com.example.bean;


import com.example.DaoUtil;
import com.example.Utils;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Console;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ty on 2017/6/13.
 */

@Table("t_project")
public class ProjectBean {

    static FormulaEvaluator evaluator;

    public static void main(String[] args) {
        String filepath = System.getProperty("user.dir") +
                "\\app\\src\\main\\java\\com\\example\\zhongdianduan";
        File file = new File(filepath);
        Console.log(file.list());
        for (String filename : file.list()) {
            String companyName = null;
            String projectName = null;
            String regex = "^" + ".*\\+";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(filename);
            while (m.find()) {
                companyName = filename.substring(m.start(), m.end() - 1);
            }

            regex = "\\+.*" + Convert.strToUnicode("重");
            p = Pattern.compile(regex);
            m = p.matcher(filename);
            while (m.find()) {
                projectName = filename.substring(m.start() + 1, m.end() - 1);
            }
            Console.log(companyName + ":" + projectName);

            Dao dao = DaoUtil.getDao();
            dao.create(ProjectBean.class, false);
            List<ProjectBean> projects = dao.query(ProjectBean.class, Cnd.where("name", "=", projectName));
            ProjectBean project = new ProjectBean();
            if (projects != null && projects.size() > 0) {
                project.setId(projects.get(0).getId());
            } else {
                project.setName(projectName);
                project.setCompany(companyName);
                project.setGmtCreate(new Date());
                project.setGmtModified(new Date());
                dao.insert(project);
            }

            filename = file.getAbsolutePath() + "\\" + filename;
            try {
                FileInputStream fis = new FileInputStream(new File(filename));
                if (filename.endsWith(".xls")) {
                    HSSFWorkbook workbook = new HSSFWorkbook(fis);
                    evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    List<ConcernBean> list = new ArrayList<>();
                    formatData(workbook, list, project.getId());
                    dao.create(ConcernBean.class, false);
                    dao.insert(list);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void formatData(HSSFWorkbook workbook, List<ConcernBean> list, int pid) {
        Iterator itSht = workbook.iterator();
        while (itSht.hasNext()) {
            Sheet sheet = (Sheet) itSht.next();
            Console.log("sheetName:" + sheet.getSheetName());
            int nameColumn = -1;
            int taskColumn = -1;
            int directionColumn = -1;
            int periodColumn = -1;
            Iterator itRow = sheet.rowIterator();
            Row row;
            while (itRow.hasNext()) {
                row = (Row) itRow.next();
                Iterator itColumn = row.cellIterator();
                Cell cell;
                while (itColumn.hasNext()) {
                    cell = (Cell) itColumn.next();
                    String val = parseAny2String(cell);
                    int column = cell.getColumnIndex();

                    if (nameColumn == -1 && val.matches("[\\s\\S]*" + Convert.strToUnicode("线名") + "[\\s\\S]*")) {
                        nameColumn = column;
                    } else if (taskColumn == -1 && val.matches("[\\s\\S]*" + Convert.strToUnicode("任务里程") + "[\\s\\S]*")) {
                        taskColumn = column;
                    } else if (directionColumn == -1 && val.matches("[\\s\\S]*" + Convert.strToUnicode("监测方法") + "[\\s\\S]*")) {
                        directionColumn = column;
                    } else if (periodColumn == -1 && val.matches("[\\s\\S]*" + Convert.strToUnicode("调整后监测周期") + "[\\s\\S]*")) {
                        periodColumn = column;
                    }

                    //有效行
                    if (val.matches("\\d+") && column == 0) {

                        ConcernBean bean = new ConcernBean();
                        bean.setProjectName(parseAny2String(row.getCell(1)));
                        String valtask = parseAny2String(row.getCell(taskColumn));
                        long[] mileArray = Utils.getMileArray(valtask);
                        if (mileArray != null) {
                            bean.setStart(mileArray[0]);
                            bean.setEnd(mileArray[1]);
                        } else if (mileArray == null) {
                            bean.setAddress(valtask);
                        }
                        bean.setName(valtask.trim());
                        bean.setDirection(parseAny2String(row.getCell(directionColumn)));

                        int period = getPeriodFromCell(parseAny2String(row.getCell(periodColumn)));
                        if (period == 0) {
                            period = getPeriodFromCell(parseAny2String(row.getCell(periodColumn - 1)));
                        }
                        bean.setPeriod(period);
                        bean.setPkPid(pid);
                        bean.setGmtCreate(new Date());
                        bean.setGmtModified(new Date());
                        if (bean.getPeriod() == 0) continue;
                        list.add(bean);
                        Console.log(bean);
                    }

                }

            }

        }
    }

    private static String parseAny2String(Cell cell) {
        if (cell == null) {
            return "null";
        }
        String result = null;
        switch (cell.getCellTypeEnum()) {
            case _NONE:
                result = "_none";
                break;
            case NUMERIC:
                result = (int) cell.getNumericCellValue() + "";
                break;
            case STRING:
                result = cell.getStringCellValue();
                break;
            case FORMULA:
                CellValue val = evaluator.evaluate(cell);
                if (val.getCellTypeEnum() == CellType.STRING) {
                    result = val.getStringValue();
                } else if (val.getCellTypeEnum() == CellType.NUMERIC) {
                    result = (int) val.getNumberValue() + "";
                } else {

                    result = 101 + "";
                }
                break;
            case BLANK:
                result = "空白";
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue() + "";
                break;
            case ERROR:
                result = "错误";
                break;
        }

        return result;
    }

    private static int getPeriodFromCell(String str) {
        if (str.matches("[\\s\\S]*" + Convert.strToUnicode("每半月") + "[\\s\\S]*")) {
            return 15;
        } else if (str.matches("[\\s\\S]*" + Convert.strToUnicode("每1月") + "[\\s\\S]*")
                || str.matches("[\\s\\S]*" + Convert.strToUnicode("每月") + "[\\s\\S]*")
                || str.matches("[\\s\\S]*" + Convert.strToUnicode("1次/月") + "[\\s\\S]*")) {
            return 30;
        } else if (str.matches("[\\s\\S]*" + Convert.strToUnicode("每2月") + "[\\s\\S]*")
                || str.matches("[\\s\\S]*" + Convert.strToUnicode("2次/月") + "[\\s\\S]*")) {
            return 60;
        } else if (str.matches("[\\s\\S]*" + Convert.strToUnicode("季度") + "[\\s\\S]*")
                || str.matches("[\\s\\S]*" + Convert.strToUnicode("每3月") + "[\\s\\S]*")) {
            return 90;
        } else if (str.equals("101")) {
            return 101;
        }
        return 0;
    }

    @Id
    @ColDefine(unsigned = true, width = 9)
    private int id;

    @Name
    private String name;

    @Column(hump = true)
    @Comment(value = "施测单位")
    private String company;//

    @Column(hump = true)
    @Comment(value = "备注")
    private String comment;//

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "创建日期")
    private Date gmtCreate;

    @Column(hump = true)
    @ColDefine(type = ColType.DATETIME)
    @Comment(value = "更改日期")
    private Date gmtModified;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
