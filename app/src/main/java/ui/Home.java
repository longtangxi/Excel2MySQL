package ui;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ui.model.BtnBean;
import ui.my.CollapsePanel;
import ui.my.FunctionButton;
import ui.my.GradientPanel;
import ui.my.Utils;
import ui.resources.Colors;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

/**
 * Created by ty on 2017/6/16.
 */

public class Home extends SingleFrameApplication {

    private Color mColorGradientLeft;//左侧渐变起点色
    private Color mColorGradientRight;//右侧渐变终点色
    private Color mColorTitleForground;//文字前景色

    @Override
    protected void initialize(String[] args) {
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void startup() {
        View view = getMainView();
        view.setComponent(createMainPanel());
        show(view);
    }

    /**
     * 构建主界面
     *
     * @return
     */
    private JComponent createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //创建顶部布局
        JPanel topPanel = new JPanel();//顶部面板
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));


        topPanel.add(createJLabel("LOGO"));
//        topPanel.add(Box.createHorizontalStrut(100));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        topPanel.add(createJLabel(sdf.format(new Date())));
        topPanel.add(new TextField("dddddddddd"));


        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel leftJPanel = getLeftPanel();


        mainPanel.add(leftJPanel, BorderLayout.WEST);

        //创建中心内容布局
        JPanel centerJPanel = new JPanel();
        centerJPanel.setLayout(new GridLayout(1, 2));
        centerJPanel.add(new JLabel("主界面"), CENTER);
        centerJPanel.add(new JLabel("主界面"), CENTER);
        mainPanel.add(centerJPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * 左侧布局
     *
     * @return
     */
    private JPanel getLeftPanel() {
        //创建左侧导航栏布局

        JPanel leftJPanel = new JPanel();
        leftJPanel.setLayout(new BorderLayout());
        leftJPanel.setBackground(Color.GREEN);
        leftJPanel.add(getLeftTitlePanel(), NORTH);///左侧功能列表标题JPanel添加至左侧功能列表JPanel
        leftJPanel.add(getLeftButtonPanel(), CENTER);
        leftJPanel.setBorder(new MatteBorder(0, 0, 0, 1,
                Colors.CONTROL_SHADOW));

        return leftJPanel;
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

        CollapsePanel collapsePanel;//折叠面板
        JPanel categoryPanel = new JPanel();//类别Item面板
        GridBagLayout categoryLayout = new GridBagLayout();
        categoryPanel.setLayout(categoryLayout);//设置类别Item布局方式
        GridBagConstraints categoryConstraints = new GridBagConstraints();
        categoryConstraints.gridx = categoryConstraints.gridy = 0;
        categoryConstraints.weightx = 1;
        categoryConstraints.fill = GridBagConstraints.HORIZONTAL;

        collapsePanel = new CollapsePanel(categoryPanel, "类别", "点击打开或者折叠");
        collapsePanel.setBorder(new CompoundBorder(
                new ChiselBorder(), new EmptyBorder(0, 0, 10, 0)));
        collapsePanel.add(categoryPanel);
        mainLayout.addLayoutComponent(collapsePanel,mainConstraints);
        panel.add(collapsePanel);
        mainConstraints.gridy++;
        List<FunctionButton> buttons = getFuncButtons();
        for (FunctionButton btn : buttons) {
            categoryLayout.addLayoutComponent(btn, categoryConstraints);
            categoryPanel.add(btn);
            categoryConstraints.gridy++;
        }
        panel.add(collapsePanel);
        JPanel blankPanel = new JPanel();
        mainConstraints.weighty = 1.0;//占据多余空间
        mainLayout.addLayoutComponent(blankPanel, mainConstraints);
        panel.add(blankPanel);
        return new JScrollPane(panel);
    }


    private List<FunctionButton> getFuncButtons() {
        List<FunctionButton> buttons = new ArrayList<>();
        String[] names = {"主页", "图表", "数据库"};
        String[] descs = {"主要功能界面", "生成各种统计图", "数据库的导入导出"};
        for (int i = 0; i < names.length; i++) {
            BtnBean bean = new BtnBean();
            bean.setName(names[i]);
            bean.setDesc(descs[i]);
//            
//            bean.setIcon(new ImageIcon(Home.class.getResource("resources/images/earth_night.gif")));
            FunctionButton button = new FunctionButton(bean);

            buttons.add(button);
        }

//        toggleButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JDialog jDialog = new JDialog(new JFrame(), "Demo JDialog", false);
//                JLabel label = new JLabel("I'm content.");
//                label.setHorizontalAlignment(JLabel.CENTER);
//                label.setPreferredSize(new Dimension(200, 140));
//                jDialog.add(label);
//                jDialog.pack();
//                jDialog.setVisible(true);
//            }
//        });
        return buttons;
    }

    /**
     * 创建左侧顶部功能菜单的标题栏
     *
     * @return
     */
    private GradientPanel getLeftTitlePanel() {
        // Calculate gradient colors for title panels
        //activeCaption[r=153,g=180,b=209]
        Color titleColor = UIManager.getColor(usingNimbus() ? "nimbusBase" : "activeCaption");

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
                new ChiselBorder(), new EmptyBorder(6, 8, 6, 8)));
        /*---------titlePanel设置完毕----*/

        JLabel title = new JLabel("功能表标题");//标题设置
        title.setOpaque(false);//透明效果
        title.setHorizontalAlignment(JLabel.LEADING);
        title.setForeground(mColorTitleForground);//设置字体颜色
        title.setBackground(Color.GRAY);
        Font labelFont = UIManager.getFont("Label.font");//设置字体大小
        labelFont = labelFont.deriveFont(Font.BOLD, labelFont.getSize() + 4f);
        title.setFont(labelFont);

        panel.add(title, CENTER);//添加至标题JPanel

        return panel;
    }

    public static boolean usingNimbus() {
        return UIManager.getLookAndFeel().getName().equals("Nimbus");
    }

    public JLabel createJLabel(String title) {
        JLabel j = new JLabel(title);
//        j.setBorder(BorderFactory.createEtchedBorder());
        j.setHorizontalAlignment(SwingConstants.CENTER);//设置字体在部件中的位置
        return j;
    }

    private static class ChiselBorder implements Border {
        private Insets insets = new Insets(1, 0, 1, 0);

        public ChiselBorder() {
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color color = c.getBackground();
            // render highlight at top顶部高亮
            g.setColor(Utils.deriveColorHSB(color, 0, 0, .2f));
            g.drawLine(x, y, x + width, y);
            // render shadow on bottom//底部阴影
            g.setColor(Utils.deriveColorHSB(color, 0, 0, -.2f));
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }
    }

    public static void main(String[] args) {
        launch(Home.class, args);
    }
}
