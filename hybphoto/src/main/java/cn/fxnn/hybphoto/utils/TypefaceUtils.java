package cn.fxnn.hybphoto.utils;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * **************************
 * Class:       TypefaceUtils
 * Author:      fangx
 * Date:        16/9/5
 * Description:  字体工具类
 * ***************************
 */
public enum TypefaceUtils {
    TYPEFACE;

    private static Typeface typeface50;
    private static Typeface typeface55;
    private static Typeface typefaceny;
    private static Typeface typefacenywcb;

    public void setFontSmall(TextView textView) {
        if (typeface50 == null)
            typeface50 = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/font_1.otf");
        textView.setTypeface(typeface50);
    }

    public void setFontBig(TextView textView) {
        if (typeface55 == null)
            typeface55 = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/font_2.otf");
        textView.setTypeface(typeface55);
    }

    public void setFontNY(TextView textView) {
        if (typefaceny == null)
            typefaceny = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/newyear.otf");
        textView.setTypeface(typefaceny);
    }

}