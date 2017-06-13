package com.example;

import org.nutz.dao.Dao;
import org.nutz.dao.util.DaoUp;

import java.io.IOException;

/**
 * Created by ty on 2017/6/13.
 */

public class DaoUtil {
    static Dao dao;

    public static Dao getDao() {
        if (dao != null) {
            return dao;
        }
        try {
            DaoUp.me().init(".\\lib\\assets\\db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dao = DaoUp.me().dao();
        return dao;
    }


}
