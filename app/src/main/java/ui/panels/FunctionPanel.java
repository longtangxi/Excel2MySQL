package ui.panels;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.TimingTrigger;
import org.jdesktop.animation.timing.triggers.TimingTriggerEvent;
import org.jdesktop.swingx.JXPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import ui.factory.BorderFactory;
import ui.model.ButtonBean;
import ui.my.Utils;
import ui.resources.Colors;

/**
 * Created by ty on 2017/6/22.
 */

public class FunctionPanel extends JXPanel {
    private static final Insets margin = new Insets(8, 10, 8, 8);

    private ButtonBean buttonBean;

    public FunctionPanel(ButtonBean buttonBean) {
        this.buttonBean = buttonBean;
        setLayout(new BorderLayout());
        // remind(aim): how to access resourceMap?
        //resourceMap = getContext().getResourceMap();

        LoadAnimationPanel loadAnimationPanel = new LoadAnimationPanel();

        add(loadAnimationPanel);
        loadAnimationPanel.setAnimating(true);

        LoadedDemoPanel demoPanel = new LoadedDemoPanel(buttonBean);

        try {
            loadAnimationPanel.setAnimating(false);
            Animator fadeOutAnimator = new Animator(400,
                    new FadeOut(FunctionPanel.this,
                            loadAnimationPanel, demoPanel));
            fadeOutAnimator.setAcceleration(.2f);
            fadeOutAnimator.setDeceleration(.3f);
            Animator fadeInAnimator = new Animator(400,
                    new PropertySetter(FunctionPanel.this, "alpha", 0.3f, 1.0f));
            TimingTrigger.addTrigger(fadeOutAnimator, fadeInAnimator, TimingTriggerEvent.STOP);
            fadeOutAnimator.start();
        } catch (Exception ignore) {
            System.err.println(ignore);
            ignore.printStackTrace();
        }
    }

    public ButtonBean getButtonBean() {
        return buttonBean;
    }

    private static class FadeOut extends PropertySetter {
        private JXPanel parent;
        private JXPanel out;
        private JComponent in;

        public FadeOut(JXPanel parent, JXPanel out, JComponent in) {
            super(out, "alpha", 1.0f, 0.3f);
            this.parent = parent;
            this.out = out;
            this.in = in;
        }

        public void end() {
            parent.setAlpha(0.3f);
            parent.remove(out);
            parent.add(in);
            parent.revalidate();
        }
    } // Fader

    private static class LoadAnimationPanel extends RoundedPanel {
        private String message;
        private int triState = 0;
        private boolean animating = false;
        private Animator animator;

        public LoadAnimationPanel() {
            super(10);
            setBorder(BorderFactory.getRoundedBorder());
            setBackground(Utils.deriveColorHSB(
                    UIManager.getColor("Panel.background"), 0, 0, -.06f));

            // remind(aim): get from resource map
            message = "buttonBean loading";

            PropertySetter rotator = new PropertySetter(this, "triState", 0, 3);
            animator = new Animator(500, Animator.INFINITE,
                    Animator.RepeatBehavior.LOOP, rotator);
            // Don't animate gears if loading is quick
            animator.setStartDelay(200);
        }

        public void setAnimating(boolean animating) {
            this.animating = animating;
            if (animating) {
                animator.start();
            } else {
                animator.stop();
            }
        }

        public boolean isAnimating() {
            return animating;
        }

        public void setTriState(int triState) {
            this.triState = triState;
            repaint();
        }

        public int getTriState() {
            return triState;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            Dimension size = getSize();

            Color textColor = Utils.deriveColorHSB(getBackground(), 0, 0, -.3f);
            Color dotColor = Utils.deriveColorHSB(textColor, 0, .2f, -.08f);
            g2.setColor(textColor);
            g2.setFont(UIManager.getFont("Label.font").deriveFont(32f));
            FontMetrics metrics = g2.getFontMetrics();
            Rectangle2D rect = metrics.getStringBounds(message, g2);
            Rectangle2D dotRect = metrics.getStringBounds(".", g2);
            float x = (float) (size.width - (rect.getWidth() + 3 * dotRect.getWidth())) / 2;
            float y = (float) (size.height - rect.getHeight()) / 2;
            g2.drawString(message, x, y);
            int tri = getTriState();
            float dx = 0;
            for (int i = 0; i < 3; i++) {
                g2.setColor(animator.isRunning() && i == tri ?
                        dotColor :
                        textColor);
                g2.drawString(".", x + (float) (rect.getWidth() + dx), y);
                dx += dotRect.getWidth();
            }
        }
    } // LoadAnimationPanel

    private static class LoadedDemoPanel extends RoundedPanel {
        private String functionName;
        private JComponent demoPanel;

        public LoadedDemoPanel(ButtonBean buttonBean) {
            super(10);
            setLayout(null);
            functionName = buttonBean.getName();
            // no description
            demoPanel = new JXPanel(new BorderLayout());
            demoPanel.add(buttonBean.createDemoComponent());
            add(demoPanel);

            applyDefaults();
        }


        @Override
        public void doLayout() {
            if (demoPanel != null) {
                Dimension size = getSize();
                Insets insets = getInsets();

//                if (descriptionArea == null) {
                    // Make buttonBean fill entire area within border
                    demoPanel.setBounds(insets.left, insets.top,
                            size.width - insets.left - insets.right,
                            size.height - insets.top - insets.bottom);
//                } else {
//                    // Split space between HTML description and running buttonBean
//                    Dimension demoSize = demoPanel.getPreferredSize();
//                    int margin = insets.top / 2;
//                    Rectangle bounds = new Rectangle();
//                    bounds.width = Math.max(demoSize.width, (int) (size.width * .50));
//                    bounds.height = Math.max(demoSize.height, size.height -
//                            2 * margin);
//                    bounds.x = size.width - bounds.width - margin;
//                    bounds.y = margin;
//                    demoPanel.setBounds(bounds);
//                    descriptionArea.setBounds(insets.left, insets.top,
//                            size.width - margin - insets.right - bounds.width,
//                            size.height - insets.top - insets.bottom);
//                }
            }
        }

        @Override
        public void updateUI() {
            super.updateUI();
            applyDefaults();
        }

        private void applyDefaults() {
            setBorder(BorderFactory.getRoundedTitleBorder(functionName, Colors.TOGGLEBUTTON_BACKGROUND, Colors.TOGGLEBUTTON_FOREGROUND));

            setFont(UIManager.getFont("titleFont"));
            Color bg = Utils.deriveColorHSB(
                    UIManager.getColor("Panel.background"), 0, 0, -.06f);
            setBackground(bg);
            setForeground(UIManager.getColor("titleForegroundColor"));
            if (demoPanel != null) {
                demoPanel.setBackground(Utils.deriveColorHSB(bg, 0, 0, .04f));
            }

        }
    }
}
