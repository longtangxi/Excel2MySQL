package ui.panels;

import com.xiaoleilu.hutool.lang.Console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ui.factory.BorderFactory;
import ui.model.ButtonBean;
import ui.my.CollapsePanel;
import ui.my.FunctionButton;
import ui.my.GradientPanel;
import ui.resources.Colors;


/**
 * Created by 87627 on 2017/6/21.
 */
public class LeftJPanel extends JPanel {

    private Color mColorGradientLeft;//左侧渐变起点色
    private Color mColorGradientRight;//右侧渐变终点色
    private Color mColorTitleForground;//文字前景色

    private ButtonBean selectedButtonBean;//当前被选中的功能按钮
    public static final String TAG_SELECTED = "selectedButtonBean";

    public LeftJPanel() {
        setLayout(new BorderLayout());
        add(getLeftTitlePanel(), BorderLayout.NORTH);///左侧功能列表标题JPanel添加至左侧功能列表JPanel
        add(getLeftButtonPanel(), BorderLayout.CENTER);
        setBorder(new MatteBorder(0, 0, 0, 1, Colors.CONTROL_SHADOW));
        addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(TAG_SELECTED)) {
                ButtonBean bean = (ButtonBean) evt.getNewValue();
                Console.log("当前选择了：" + bean.getName());
                setSelectedButtonBean(bean);
            }
        });
    }

    /**
     * 创建左侧顶部功能菜单的标题栏
     *
     * @return
     */
    private GradientPanel getLeftTitlePanel() {
        // Calculate gradient colors for title panels
        //activeCaption[r=153,g=180,b=209]
        Color titleColor = UIManager.getColor("activeCaption");

        // Some LAFs (e.g. GTK) don't contain "activeCaption"
        if (titleColor == null) {
            titleColor = Colors.CONTROL;
        }
        //hue（色相）、saturation（饱和度）、brightness（明度）。
        //{0.5863095,0.26794258,0.81960785}
        float hsb[] = Color.RGBtoHSB(
                titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), null);

        mColorGradientLeft = Color.getHSBColor(hsb[0] - .013f, .15f, .85f);
        mColorGradientRight = Color.getHSBColor(hsb[0] - .005f, .24f, .80f);
        mColorTitleForground = Color.getHSBColor(hsb[0], .54f, .40f);
        //java.awt.Color[r=184,g=202,b=217]
        //java.awt.Color[r=155,g=180,b=204]
        GradientPanel panel = new GradientPanel(
                mColorGradientLeft,
                mColorGradientRight);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new CompoundBorder(
                BorderFactory.getChiselBorder(), new EmptyBorder(6, 8, 6, 8)));
        /*---------titlePanel设置完毕----*/

        JLabel title = new JLabel("功能表标题");//标题设置
        title.setOpaque(false);//透明效果
        title.setHorizontalAlignment(JLabel.LEADING);
        title.setForeground(mColorTitleForground);//设置字体颜色
        title.setBackground(Color.GRAY);
        Font labelFont = UIManager.getFont("Label.font");//设置字体大小
        labelFont = labelFont.deriveFont(Font.BOLD, labelFont.getSize() + 4f);
        title.setFont(labelFont);

        panel.add(title, BorderLayout.CENTER);//添加至标题JPanel

        return panel;
    }

    /**
     * 创建左侧功能选项栏
     *
     * @return
     */
    private JScrollPane getLeftButtonPanel() {

        JPanel panel = new JPanel();//按钮主面板
        GridBagLayout mainLayout = new GridBagLayout();
        panel.setLayout(mainLayout);
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = mainConstraints.gridy = 0;//索引置为0
        mainConstraints.fill = GridBagConstraints.HORIZONTAL;//水平方向可扩大
        mainConstraints.weightx = 1;

        JPanel categoryPanel = new JPanel();//类别Item面板
        GridBagLayout categoryLayout = new GridBagLayout();
        categoryPanel.setLayout(categoryLayout);//设置类别Item布局方式
        GridBagConstraints categoryConstraints = new GridBagConstraints();
        categoryConstraints.gridx = categoryConstraints.gridy = 0;
        categoryConstraints.weightx = 1;
        categoryConstraints.fill = GridBagConstraints.HORIZONTAL;

        CollapsePanel collapsePanel;//折叠面板
        collapsePanel = new CollapsePanel(categoryPanel, "功能表", "点击打开或者折叠");
        collapsePanel.setBorder(new CompoundBorder(
                BorderFactory.getChiselBorder(), new EmptyBorder(0, 0, 10, 0)));
        collapsePanel.setFont(UIManager.getFont("CheckBox.font").deriveFont(Font.BOLD));
        collapsePanel.setForeground(mColorTitleForground);

        mainLayout.addLayoutComponent(collapsePanel, mainConstraints);
        panel.add(collapsePanel);
        mainConstraints.gridy++;

        List<FunctionButton> buttons = getFuncButtons();
        for (FunctionButton btn : buttons) {

            btn.addActionListener(e -> {
                FunctionButton button = (FunctionButton) e.getSource();
                setSelectedButtonBean(button.getmButtonBean());
//
//                panel.updateUI();
//                panel.revalidate();
            });
            categoryLayout.addLayoutComponent(btn, categoryConstraints);
            categoryConstraints.gridy++;
            categoryPanel.add(btn);

        }
        panel.add(collapsePanel);
        //占据底部多余空间
        JPanel blankPanel = new JPanel();
        mainConstraints.weighty = 1.0;
        mainLayout.addLayoutComponent(blankPanel, mainConstraints);
        panel.add(blankPanel);
        JScrollPane jScrollPane = new JScrollPane(panel);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return new JScrollPane(jScrollPane);
    }

    private void setSelectedButtonBean(ButtonBean buttonBean) {
        ButtonBean oldSelectedButtonBean = selectedButtonBean;
        selectedButtonBean = buttonBean;
        firePropertyChange(TAG_SELECTED, oldSelectedButtonBean, buttonBean);
    }


    private List<FunctionButton> getFuncButtons() {
        List<FunctionButton> buttons = new ArrayList<>();
        String[] names = {"主页", "图表", "数据库"};
        String[] descs = {"主要功能界面", "生成各种统计图", "数据库的导入导出"};
        for (int i = 0; i < names.length; i++) {
            ButtonBean bean = new ButtonBean();
            bean.setName(names[i]);
            bean.setDesc(descs[i]);

//            bean.setIcon(new ImageIcon(Home.class.getResource("resources/images/earth_night.gif")));
            buttons.add(new FunctionButton(bean));
        }
        return buttons;
    }

}
