package ui.my;

import java.awt.Color;
import java.awt.Font;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * 打印系统属性
 * Created by ty on 2017/6/21.
 */

public class PrintProperties {

    public static void main(String[] args) {
        PrintProperties printProperties = new PrintProperties();
    }

    public PrintProperties() {
        System.out.println("\n------------------printProperties Start------------------\n");

        // UIManager Keys
        Enumeration e = UIManager.getLookAndFeelDefaults().keys();
        ArrayList listFont = new ArrayList();
        ArrayList listIcon = new ArrayList();
        ArrayList listColor = new ArrayList();
        ArrayList listBorder = new ArrayList();
        ArrayList listElse = new ArrayList();

        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            if (UIManager.get(key) instanceof Font) {
                listFont.add(key);
            } else if (UIManager.get(key) instanceof Icon) {
                listIcon.add(key);
            } else if (UIManager.get(key) instanceof Color) {
                listColor.add(key);
            } else if (UIManager.get(key) instanceof Border) {
                listBorder.add(key);
            } else {
                listElse.add(key);
            }
        }

        System.out.println("\n\n-----------------------Font-----------------------\n");
        printArrayList(listFont);

        System.out.println("\n\n-----------------------Icon-----------------------\n");
        printArrayList(listIcon);

        System.out.println("\n\n-----------------------Color-----------------------\n");
        printArrayList(listColor);

        System.out.println("\n\n-----------------------Border-----------------------\n");
        printArrayList(listBorder);

        System.out.println("\n\n\n-----------------------Else-----------------------\n");
        printArrayList(listElse);

        // 系统属性
        System.out.println("\n\n\n\n\n\n\n\n\n系统属性");
        Properties property = System.getProperties();
        Enumeration keys = property.keys();
        int i = 0;
        DecimalFormat df = new DecimalFormat("Key0000");
        while (keys.hasMoreElements()) {
            i++;
            String key = (String) keys.nextElement();
            System.out.println(df.format(i) + ":" + key + " -> " + System.getProperty(key));
        }

        // 本机支持的全部编码集名字
        System.out.println("\n\n\n\n\n\n\n\n\n本机支持的全部编码集名字");
        Iterator iterator = Charset.availableCharsets().keySet().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("\n------------------printProperties End------------------\n");
    }

    private void printArrayList(ArrayList list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i)+"--->"+UIManager.get(list.get(i)).toString());
        }
    }
}
