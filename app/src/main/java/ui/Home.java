package ui;

import com.xiaoleilu.hutool.lang.Console;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ui.factory.BorderFactory;
import ui.model.ButtonBean;
import ui.my.AnimatingSplitPane;
import ui.panels.FunctionPanel;
import ui.panels.LeftJPanel;
import ui.test.FirstFunctionPanel;

import static java.awt.BorderLayout.CENTER;

/**
 * Created by ty on 2017/6/16.
 */

public class Home extends SingleFrameApplication {

    private final String TAG = Home.class.getSimpleName() + ">>>";

    private JPanel mainPanel;

    public static final int MAIN_FRAME_HEIGHT = 640;
    public static final int MAIN_FRAME_WIDTH = 880;
    public static final int LEFT_PANEL_WIDTH = 186;

    public static final int DEMO_PANEL_HEIGHT = 400;
    public static final int DEMO_PANEL_WIDTH = MAIN_FRAME_WIDTH - LEFT_PANEL_WIDTH;

    private ButtonBean currentButtonBean;//当前被选中的按钮
    private JPanel contentJPanel = new JPanel();//盛裝内容的面板
    private JComponent mCurrentFunctionPanel;//当前所选的功能面板
    private HashMap<String, FunctionPanel> runningContainerCache = new HashMap<>();//存储功能名称-功能面板

    private final String TAG_CONTAINERHOLDER = "contentJPanel";
    private final String TAG_CURRENT_BUTTON_BEAN = "currentButtonBean";

    private List<ButtonBean> buttonList = new ArrayList<>();

    private AnimatingSplitPane contentHorizonalSpliteJPanel;//分割面板，用于盛装功能面板和描述面板

    @Override
    protected void initialize(String[] args) {
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setFunctionList();
        runningContainerCache = new HashMap<>();
        setContentJPanel(new JPanel());
    }

    private void setFunctionList() {
        String[] names = {"主页", "图表", "数据库"};
        String[] descs = {"主要功能界面", "生成各种统计图", "数据库的导入导出"};
        for (int i = 0; i < names.length; i++) {
            ButtonBean bean = new ButtonBean();
            bean.setName(names[i]);
            bean.setDesc(descs[i]);
            buttonList.add(bean);
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
        /*——————左侧功能按钮面板——————*/
        JPanel leftJPanel = new LeftJPanel(buttonList);
        leftJPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, MAIN_FRAME_HEIGHT));
        leftJPanel.addPropertyChangeListener(evt -> {
            //点击功能键按钮会触发
            Console.log(TAG + "功能键按钮点击事件已触发");
            if (evt.getPropertyName().equals(LeftJPanel.TAG_SELECTED)) {
                Console.log(TAG + "并且propertyName：" + LeftJPanel.TAG_SELECTED);
                //记录当前所选功能
                setCurrentFunction((ButtonBean) evt.getNewValue());
            }
        });
        mainPanel.add(leftJPanel, BorderLayout.WEST);
        /*——————左侧功能按钮面板——————*/

        /*——————右侧功能界面面板——————*/
        contentHorizonalSpliteJPanel = new AnimatingSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        contentHorizonalSpliteJPanel.setBorder(BorderFactory.getEmptyBorder());
        mainPanel.add(contentHorizonalSpliteJPanel, BorderLayout.CENTER);
        JPanel centerJPanel = new JPanel();
        centerJPanel.setLayout(new BorderLayout());
        centerJPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerJPanel.setPreferredSize(new Dimension(DEMO_PANEL_WIDTH, DEMO_PANEL_HEIGHT));

        centerJPanel.add(contentJPanel);
        contentHorizonalSpliteJPanel.setLeftComponent(centerJPanel);
        mCurrentFunctionPanel = contentJPanel;
        centerJPanel.add(mCurrentFunctionPanel, BorderLayout.CENTER);

        JPanel descJPanel = new JPanel();
        descJPanel.add(new JLabel("用于描述的窗口"));
        descJPanel.setMinimumSize(new Dimension(50, 50));
        contentHorizonalSpliteJPanel.setRightComponent(descJPanel);
        /*——————右侧功能界面面板——————*/


        return mainPanel;
    }

    /**
     * 更新内容面板
     *
     * @param contentJPanel
     */
    private void setContentJPanel(JPanel contentJPanel) {
        JPanel oldContentJPanel = this.contentJPanel;

        this.contentJPanel = contentJPanel;

        firePropertyChange(TAG_CONTAINERHOLDER, oldContentJPanel, contentJPanel);
    }

    /**
     * 选择某个功能时，调用此方法
     * 根据传递的实体类生成对应的内容面板
     * 更新界面
     *
     * @param buttonBean
     */
    private void setCurrentFunction(ButtonBean buttonBean) {
        if (buttonBean == currentButtonBean) {
            return;
        }
        ButtonBean oldCurrentButtonBean = currentButtonBean;
        currentButtonBean = buttonBean;
        Console.log("oldBean:" + oldCurrentButtonBean.getName() + "  newBean:" + currentButtonBean.getName() + " containerHolder的内容:" + contentJPanel.getComponentCount());
        if (buttonBean == null) {
            contentJPanel.add(mCurrentFunctionPanel, CENTER);
        } else {

            FunctionPanel panel = runningContainerCache.get(buttonBean.getName());
            if (panel == null) {
                Console.log(TAG + "缓存的panel为空");
                if (buttonBean.getName().equals("主页")) {
                    Console.log(TAG + "新建主页panel");
                    panel = new FirstFunctionPanel(buttonBean);
                    runningContainerCache.put(buttonBean.getName(), panel);
                } else if (buttonBean.getName().equals("图表")) {

                    Console.log(TAG + "新建图表panel");
                    panel = new FirstFunctionPanel(buttonBean);
                    runningContainerCache.put(buttonBean.getName(), panel);
                } else if (buttonBean.getName().equals("数据库")) {

                    Console.log(TAG + "新建数据库panel");
                    panel = new FirstFunctionPanel(buttonBean);
                    runningContainerCache.put(buttonBean.getName(), panel);
                }
//                buttonBean.startInitializing();
//                panel = new FunctionPanel(buttonBean);
//                panel.setPreferredSize(new Dimension(100, 100));//TODO
//                runningContainerCache.put(buttonBean.getName(), panel);
            }
            if (mCurrentFunctionPanel != null && panel != mCurrentFunctionPanel) {
                Console.log(TAG + "当前panel不为空，开始移除");
                contentJPanel.remove(mCurrentFunctionPanel);
            }
            mCurrentFunctionPanel = panel;
            contentJPanel.add(mCurrentFunctionPanel, CENTER);
            contentJPanel.revalidate();
            contentJPanel.repaint();
            //TODO
        }
//        firePropertyChange(TAG_CURRENT_BUTTON_BEAN, oldCurrentButtonBean, buttonBean);
    }


    public static void main(String[] args) {
        launch(Home.class, args);
    }
}
