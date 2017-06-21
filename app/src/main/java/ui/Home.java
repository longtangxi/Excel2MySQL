package ui;

import com.xiaoleilu.hutool.lang.Console;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ui.model.ButtonBean;
import ui.panels.LeftJPanel;

import static java.awt.BorderLayout.CENTER;

/**
 * Created by ty on 2017/6/16.
 */

public class Home extends SingleFrameApplication {

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
    private final String TAG_CONTAINERHOLDER = "containerHolder";

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

    private void setContainerHolder(JComponent containerHolder) {
        JComponent oldContainerHolder = this.containerHolder;
        this.containerHolder = containerHolder;
        firePropertyChange(TAG_CONTAINERHOLDER, oldContainerHolder, containerHolder);
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

        JPanel leftJPanel = new LeftJPanel();
        leftJPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, MAIN_FRAME_HEIGHT));
        leftJPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(LeftJPanel.TAG_SELECTED)){
                    setCurrentFunction((ButtonBean) evt.getNewValue());
                }
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
        Console.log("setCurrentFunction:"+buttonBean.toString());
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

//        firePropertyChange("currentButtonBean", oldCurrentButtonBean, currentButtonBean);
    }




    public static void main(String[] args) {
        launch(Home.class, args);
    }
}
