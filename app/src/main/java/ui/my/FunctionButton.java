package ui.my;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import ui.model.BtnBean;

/**
 * Created by ty on 2017/6/19.
 */

public class FunctionButton extends JToggleButton {
    private BtnBean mBtnBean;

    public FunctionButton(BtnBean bean) {
        super();
        this.mBtnBean = bean;
        String name = mBtnBean.getName();
        setText(name);
        setIcon(mBtnBean.getIcon());
        setIconTextGap(10);
        setHorizontalTextPosition(JToggleButton.TRAILING);
        setHorizontalAlignment(JToggleButton.LEADING);
        setOpaque(false);//设置不透明
        setBorder(new CompoundBorder(
                new DefaultBorder(), new EmptyBorder(0, 0, 0, 0)));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setToolTipText(mBtnBean.getDesc());
    }

    @Override
    public void updateUI() {
        super.updateUI();
        // some look and feels replace our border, so take it back
        setBorder(new DefaultBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected()) {
            setBackground(UIManager.getColor("Tree.selectionBackground"));
            g.setColor(UIManager.getColor("Tree.selectionBackground"));
            Dimension size = getSize();
            g.fillRect(0, 0, size.width, size.height);
            setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            setBackground(UIManager.getColor("ToggleButton.background"));
            Color foreground = UIManager.getColor("ToggleButton.foreground");
//            switch (mBtnBean.getState()) {
//                case STOPPED: {
//                    foreground = visitedForeground;
//                    break;
//                }
//                case FAILED: {
//                    foreground = failedForeground;
//                }
//            }
            setForeground(foreground);
        }
        super.paintComponent(g);
    }

    public BtnBean getmBtnBean() {
        return mBtnBean;
    }

    private static class DefaultBorder implements Border {
        private Insets insets = new Insets(2, 1, 1, 1);

        public DefaultBorder() {
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            AbstractButton b = (AbstractButton) c;
            if (b.isSelected()) {
                Color color = c.getBackground();
                g.setColor(Utils.deriveColorHSB(color, 0, 0, -.20f));
                g.drawLine(x, y, x + width, y);
                g.setColor(Utils.deriveColorHSB(color, 0, 0, -.10f));
                g.drawLine(x, y + 1, x + width, y + 1);
                g.drawLine(x, y + 2, x, y + height - 2);
                g.setColor(Utils.deriveColorHSB(color, 0, 0, .24f));
                g.drawLine(x, y + height - 1, x + width, y + height - 1);
            }
        }
    }


}

