package com.example.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/**
 * Created by ty on 2017/6/16.
 */

public class UIHome {

    JFrame jFrame = new JFrame("测试");

    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    private void init() {
//        jFrame.setLayout(new BoxLayout(jFrame, BoxLayout.Y_AXIS));
//        JPanel topPanel = new JPanel();

//        Box horizonalBox = Box.createHorizontalBox();
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
//        //为EtchedBorder边框添加标题
//        horizonalBox.add(new JLabel("app"));
//        horizonalBox.add(new JLabel("时间"));
//        horizonalBox.add(new JLabel("搜索"));

//        topPanel.setBorder(new TitledBorder(
//                new EtchedBorder()
//                , "测试"
//                , TitledBorder.CENTER, TitledBorder.TOP));
//        topPanel.add(new JLabel("app"));

//        topPanel.setLayout(new GridLayout(1,4,4,4));
//        topPanel.add(new JLabel("app"));
//        topPanel.add(new JLabel("app"));
//        topPanel.add(new JLabel("app"));
//        topPanel.add(new JLabel("app"));


        jFrame.setLayout(gb);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        addButton(new JLabel("app"));

        gbc.weightx = 2;
        addButton(new JLabel("日期"));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        addButton(new JLabel("搜索"));
        gbc.weightx = 0;
        addButton(new JLabel("侧边栏"));

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private void addButton(JLabel jLabel) {
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setBorder(new EtchedBorder());
        gb.setConstraints(jLabel, gbc);
        jFrame.add(jLabel);
    }


    public static void main(String[] args) {
        new UIHome().init();
    }
}
