package ui;

import com.xiaoleilu.hutool.lang.Console;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import ui.model.FuncBean;
import ui.my.GradientPanel;
import ui.my.ToggleButton;
import ui.my.Utils;

import static java.awt.BorderLayout.CENTER;
import static java.awt.GridBagConstraints.NORTH;

/**
 * Created by ty on 2017/6/16.
 */

public class Home extends SingleFrameApplication {

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
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel leftJPanel = new JPanel(gb);
        leftJPanel.setBackground(Color.RED);
        gbc.gridx = gbc.gridy = 0;//设置索引
        gbc.anchor = NORTH;
//        gbc.gridwidth = 2;//横向跨越2格
//        gbc.gridheight = 1;//纵向跨越1格
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        GradientPanel titlePanel = getLeftTitlePanel();//左侧功能列表标题JPanel
        gb.setConstraints(titlePanel, gbc);
        JLabel title = new JLabel("功能表标题");//标题设置
        title.setOpaque(false);
        title.setHorizontalAlignment(JLabel.LEADING);
        titlePanel.add(title, BorderLayout.CENTER);//添加至标题JPanel

        leftJPanel.add(titlePanel);//添加至左侧功能列表JPanel

        List<ToggleButton> buttons = getFuncButtons(leftJPanel);

        for (ToggleButton btn : buttons) {
            gbc.gridy++;
            Console.log(gbc.gridy);
            gb.setConstraints(btn,gbc);
            leftJPanel.add(btn);
        }
        return leftJPanel;
    }

    private List<ToggleButton> getFuncButtons(JPanel leftJPanel) {
        List<ToggleButton> buttons = new ArrayList<>();
        String[] names = {"主页", "图表", "数据库"};
        String[] descs = {"主要功能界面", "生成各种统计图", "数据库的导入导出"};
        for (int i = 0; i < names.length; i++) {
            FuncBean bean = new FuncBean();
            bean.setName(names[i]);
            bean.setDesc(descs[i]);
            bean.setIcon(new ImageIcon(Home.class.getResource("resources/images/earth_night.gif")));
            ToggleButton button = new ToggleButton(bean);

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

    private GradientPanel getLeftTitlePanel() {
        // Calculate gradient colors for title panels
        Color titleColor = UIManager.getColor(usingNimbus() ? "nimbusBase" : "activeCaption");

        // Some LAFs (e.g. GTK) don't contain "activeCaption"
        if (titleColor == null) {
            titleColor = UIManager.getColor("control");
        }
        float hsb[] = Color.RGBtoHSB(
                titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), null);
        String COLOR_1 = "color1";
        String COLOR_2 = "color1";
        String COLOR_FOREGROUND = "color_forground";
        UIManager.put(COLOR_1,
                Color.getHSBColor(hsb[0] - .013f, .15f, .85f));
        UIManager.put(COLOR_2,
                Color.getHSBColor(hsb[0] - .005f, .24f, .80f));
        UIManager.put(COLOR_FOREGROUND,
                Color.getHSBColor(hsb[0], .54f, .40f));

        GradientPanel titlePanel = new GradientPanel(
                UIManager.getColor(COLOR_1),
                UIManager.getColor(COLOR_2));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new CompoundBorder(
                new ChiselBorder(), new EmptyBorder(6, 8, 6, 10)));
        return titlePanel;
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
            // render highlight at top
            g.setColor(Utils.deriveColorHSB(color, 0, 0, .2f));
            g.drawLine(x, y, x + width, y);
            // render shadow on bottom
            g.setColor(Utils.deriveColorHSB(color, 0, 0, -.2f));
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }
    }

    public static void main(String[] args) {
        launch(Home.class, args);
    }
}
