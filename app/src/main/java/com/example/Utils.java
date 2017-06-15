package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ty on 2017/6/15.
 */

public class Utils {
    public static long[] getMileArray(String str) {
        long[] result = new long[2];
        String regex = "[K|k].*\\d{1,3}";


        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String mile = str.substring(m.start(), m.end());//K647+050-K647+350
            mile = mile.replaceAll("[k|K]", "");//647+050-647+350
            String[] startAndEnd = mile.split("[~|-]");//[647+050,647+350]
            if (startAndEnd.length == 2) {
                String[] startStr = startAndEnd[0].split("\\+");
                long startLong = Long.parseLong(startStr[0]) * 1000 + Long.parseLong(startStr[1]);
                result[0] = startLong;
                String[] endStr = startAndEnd[1].split("\\+");
                long endLong = Long.parseLong(endStr[0]) * 1000 + Long.parseLong(endStr[1]);
                result[1] = endLong;
                return result;
            }
        }
        return null;
    }
}
