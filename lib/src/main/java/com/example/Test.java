package com.example;


import org.nutz.dao.Dao;
import org.nutz.dao.util.DaoUp;

import java.io.IOException;

/**
 * Created by ty on 2017/6/8.
 */

public class Test {

    public static void main(String[] args) {


        try {
            DaoUp.me().init(".\\lib\\assets\\db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dao dao = DaoUp.me().dao();
        dao.create(Person.class, true);
        Person p = new Person();
        p.setName("Tom2");
        p.setAge(56);
        dao.insert(p);
//        Ioc ioc = new NutIoc(new JsonLoader("\\lib\\src\\dao.js"));
//        DataSource ds = ioc.get(DataSource.class);
//        Dao dao = new NutDao(ds);
//        ioc.get(Dao.class);
//        dao.create(Person.class, true);
//        Person p = new Person();
//        p.setName("Tom");
//        dao.insert(p);
//        ioc.depose();//关闭Ioc容器

//        SimpleDataSource dataSource = new SimpleDataSource();
//        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1/st_work");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        // 创建一个NutDao实例,在真实项目中, NutDao通常由ioc托管, 使用注入的方式获得
//        Dao dao = new NutDao(dataSource);
//        dao.create(Person.class, false);
//        Person p = new Person();
//        p.setAge(45);
//        p.setName("ABC");
//        dao.insert(p);
//        Console.log(p.getId());

//        Date d = null;
//        try {
//            Console.log(d.after(new Date()));
//
//        } catch (NullPointerException ne) {
//            Console.log("空指针异常");
//        } catch (Exception e) {
//            Console.log("未知异常");
//        }
//        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        String pname = "项目名称";
//        String pmile = "项目里程";
//        String ptype = "项目类型";
//        String paddr = "施工地点";
//        String plevel = "测量等级";
//        map.put(pname, Convert.strToUnicode(pname) + "[\\u0391-\\uFFE5]+\\u7ebf");
//        map.put(pmile, "[k|K].*\\+\\d{1,3}");
//        map.put(ptype, "\\d{1}[\\u0391-\\uFFE5]+"+Convert.strToUnicode(paddr));
//        map.put(paddr, Convert.strToUnicode(paddr)+"[\\u0391-\\uFFE5]+"+Convert.strToUnicode(plevel));
//        map.put(plevel, Convert.strToUnicode(plevel)+"[\\u0391-\\uFFE5]+\\u7b49");
//        String src = "项目名称：杭深线K646+849～K647+642重点沉降观测地段            施工地点：苍南站                  测量等级：二等                   第 1 页  共 1 页";
//        src = src.replaceAll("\\s+", "");//去除所有空格
//        src = src.replaceAll("[\\uff1a|\\u003a]", "");
//
//        Console.log(src);
//        int start;
//        int end;
//        Iterator it = map.keySet().iterator();
//        while (it.hasNext()) {
//            String type = (String) it.next();
//            Pattern p = Pattern.compile(map.get(type));
//            Matcher m = p.matcher(src);
//
//            while (m.find()) {
//                String res = "";
//                start = m.start();
//                end = m.end();
//                if (type.equals(pname)) {
//                    res = src.substring(start + type.length(), end);
//                } else if (type.equals(pmile)) {
//                    res = src.substring(start, end);
//                } else if (type.equals(ptype)) {
//                    res = src.substring(start+1,end-paddr.length());
//                }else if (type.equals(paddr)){
//                    res = src.substring(start+paddr.length(),end-plevel.length());
//                }else if (type.equals(plevel)){
//                    res = src.substring(start+plevel.length(),end);
//                }
//                Console.log(type);
//                Console.log(res);
//            }
//    }


//        }


//        for (int i = 0; i < A.length; i++) {
//
//            String input = A[i];
//            Pattern p = Pattern.compile(input);
//            Matcher m = p.matcher(src);
//            while (m.find()){
//                Console.log(input);
//                Console.log(i+" 起始位置："+m.start()+"  结束位置："+m.end());
//                Console.log(src.substring(m.start()+1,m.end()));
//            }
//        }

    }
}
