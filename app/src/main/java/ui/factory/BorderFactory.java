package ui.factory;

import java.awt.Color;

import javax.swing.border.Border;

/**
 * Created by 87627 on 2017/6/21.
 */
public class BorderFactory {
    private static ChiselBorder mChiselBorder;
    private static RoundedBorder mRoundedBorder;
    private static RoundedTitleBorder mRoundedTitleBorder;

    public static final Border getChiselBorder() {
        if (mChiselBorder == null) {
            mChiselBorder = new ChiselBorder();
        }
        return mChiselBorder;
    }

    public static final Border getRoundedBorder() {
        if (mRoundedBorder == null) {
            mRoundedBorder = new RoundedBorder();
        }
        return mRoundedBorder;
    }

    public static final Border getRoundedTitleBorder(String title, Color firstColor, Color endColor) {
        if (mRoundedTitleBorder == null) {
            mRoundedTitleBorder = new RoundedTitleBorder(title, firstColor, endColor);
        }
        return mRoundedTitleBorder;
    }


}
