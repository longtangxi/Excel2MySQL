package com.example.table;

/**
 * Created by ty on 2017/6/6.
 */


import com.example.DBManager;

/**
 * 重点段记录表
 */
public class Focus extends BaseTable {
    //测试新
    public static String tableName = Focus.class.getSimpleName().toLowerCase();
    public static String NAME = "name";//名称
    public static String PERIOD = "period";//周期
    public static String STATUS = "status";//是否开启
    public static String PROJECT_ID = "project_id";//是否开启
    public static String CONTENT = "content";//工作内容，垂向or横向


    public static void main(String[] args) {

        StringBuilder createTableSQL = new StringBuilder();

        createTableSQL.append("CREATE TABLE IF NOT EXISTS " + tableName)
                .append("( ")
                .append(ID + " int unsigned auto_increment primary key")
                .append("," + NAME + " varchar(100) NOT NULL COMMENT '名称'")
                .append("," + PERIOD + " tinyint unsigned NOT NULL COMMENT '周期'")
                .append("," + STATUS + " tinyint unsigned NOT NULL COMMENT '是否开启'")
                .append("," + PROJECT_ID + " tinyint unsigned NOT NULL COMMENT '所属工程号'")
                .append("," + CONTENT + " tinyint unsigned NOT NULL COMMENT '0为横向，1为垂向'")
                .append("," + GMT_CREATE + " datetime NOT NULL")
                .append("," + GMT_MODIFIED + " datetime NOT NULL")
                .append(" )");


        DBManager manager = DBManager.getInstance();
        try {

            manager.getInstance().executeUpdate(createTableSQL.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
