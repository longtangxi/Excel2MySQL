package ui;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ui.model.ButtonBean;
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
    private ButtonBean selectedButtonBean;//当前被选中的按钮
    private JPanel mainPanel;

    public static final int MAIN_FRAME_HEIGHT = 640;
    public static final int MAIN_FRAME_WIDTH = 880;
    public static final int LEFT_PANEL_WIDTH = 186;

    public static final int DEMO_PANEL_HEIGHT = 400;
    public static final int DEMO_PANEL_WIDTH = MAIN_FRAME_WIDTH - LEFT_PANEL_WIDTH;

    private ButtonBean currentButtonBean;
    private JPanel centerJPanel;
    private JComponent containerHolder;
    private JComponent currentContainerHolder;
    private HashMap<String, JComponent> runningContainerCache = new HashMap<>();

    @Override
    protected void initialize(String[] args) {
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        runningContainerCache = new HashMap<>();
        setContainerHolder(new JLabel("初始"));
    }

    private void setContainerHolder(JComponent jComponent) {
        JComponent oldContainerHolder = this.containerHolder;
        this.containerHolder = jComponent;
        firePropertyChange("containerHolder", oldContainerHolder, jComponent);
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

//        //创建顶部布局
//        JPanel topPanel = new JPanel();//顶部面板
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
//
//
//        topPanel.add(createJLabel("LOGO"));
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//        topPanel.add(createJLabel(sdf.format(new Date())));
//        topPanel.add(new TextField("dddddddddd"));
//
//
//        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
////        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel leftJPanel = getLeftPanel();
        leftJPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, MAIN_FRAME_HEIGHT));
        leftJPanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("selectedButtonBean")) {
                setCurrentFunction((ButtonBean) evt.getNewValue());
            }
        });
        mainPanel.add(leftJPanel, BorderLayout.WEST);
        centerJPanel = new JPanel();
        centerJPanel.setLayout(new BorderLayout());
        centerJPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerJPanel.setPreferredSize(new Dimension(DEMO_PANEL_WIDTH, DEMO_PANEL_HEIGHT));
        centerJPanel.add(new JLabel("测试测试测试测试测试测试测试"));

        //创建中心内容布局
        mainPanel.add(centerJPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private void setCurrentFunction(ButtonBean buttonBean) {
        if (buttonBean == currentButtonBean) {
            return;
        }
        ButtonBean oldCurrentButtonBean = currentButtonBean;
        currentButtonBean = buttonBean;
        if (currentButtonBean != null) {
            JComponent jComponent = runningContainerCache.get(buttonBean.getName());
            if (jComponent == null) {
                currentButtonBean.startInitializing();
                jComponent = new JLabel(currentButtonBean.getName());
                jComponent.setPreferredSize(new Dimension(100, 100));//TODO
                runningContainerCache.put(currentButtonBean.getName(), jComponent);
            }
            containerHolder.remove(currentContainerHolder);
            currentContainerHolder = jComponent;
            containerHolder.add(currentContainerHolder, CENTER);
            containerHolder.revalidate();
            containerHolder.repaint();
            getMainFrame().validate();
            //TODO
        }
        if (currentButtonBean == null) {
            containerHolder.add(new JLabel("currentButtonBean == null"), CENTER);
        }

        firePropertyChange("currentButtonBean", oldCurrentButtonBean, currentButtonBean);
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
                new ChiselBorder(), new EmptyBorder(0, 0, 10, 0)));
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

                panel.updateUI();
                panel.revalidate();
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


    private List<FunctionButton> getFuncButtons() {
        List<FunctionButton> buttons = new ArrayList<>();
        String[] names = {"主页", "图表", "数据库"};
        String[] descs = {"主要功能界面", "生成各种统计图", "数据库的导入导出"};
        for (int i = 0; i < names.length; i++) {
            ButtonBean bean = new ButtonBean();
            bean.setName(names[i]);
            bean.setDesc(descs[i]);

            bean.setIcon(new ImageIcon(Home.class.getResource("resources/images/earth_night.gif")));
            buttons.add(new FunctionButton(bean));
        }
        return buttons;
    }

    public ButtonBean getSelectedButtonBean() {
        return selectedButtonBean;
    }

    private void setSelectedButtonBean(ButtonBean buttonBean) {

        ButtonBean oldSelectedButtonBean = selectedButtonBean;
        selectedButtonBean = buttonBean;
        firePropertyChange("selectedButtonBean", oldSelectedButtonBean, buttonBean);
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
