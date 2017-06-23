package ui.factory;

import java.awt.Color;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Created by 87627 on 2017/6/21.
 */
public class BorderFactory {
    private static ChiselBorder mChiselBorder;
    private static RoundedBorder mRoundedBorder;
    private static RoundedTitleBorder mRoundedTitleBorder;
    private static EmptyBorder mEmptyBorder;

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

    public static final Border getEmptyBorder() {
        if (mEmptyBorder == null) {
            mEmptyBorder = new EmptyBorder(0, 0, 0, 0);
        }
        return mEmptyBorder;
    }


}
