package ui.factory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import ui.my.Utils;

/**
 * Created by 87627 on 2017/6/21.
 */
class ChiselBorder implements Border {
    private Insets insets = new Insets(1, 0, 1, 0);

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
