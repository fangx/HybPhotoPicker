package cn.fxnn.hybphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * **************************
 * Class:       PhotoItemLayout
 * Author:      fangx
 * Date:        16/9/2
 * Description:
 * ***************************
 */
public class PhotoItemLayout extends FrameLayout {
    public PhotoItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PhotoItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoItemLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        heightMeasureSpec =
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
