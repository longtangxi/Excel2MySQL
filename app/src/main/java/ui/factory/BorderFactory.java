package ui.factory;

import javax.swing.border.Border;

/**
 * Created by 87627 on 2017/6/21.
 */
public class BorderFactory {
    private static ChiselBorder mChiselBorder;

    public static final Border getChiselBorder() {
        if (mChiselBorder == null) {
            mChiselBorder = new ChiselBorder();
        }
        return mChiselBorder;
    }


}
