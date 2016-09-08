package cn.fxnn.hybphoto.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListView;
import android.widget.PopupWindow;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.adapter.FolderListAdapter;

/**
 * **************************
 * Class:       FolderPopupWindow
 * Author:      fangx
 * Date:        16/9/7
 * Description:
 * ***************************
 */
public class FolderPopupWindow extends PopupWindow {

    private ListView listView;

    private View bg_view;

    private View anchorView;

    private Context context;

    private PopupWindowHideListener popupWindowHideListener;

    //为了消除白边设置widow偏移5个像素
    public static int POP_OFFSET = 5;

    public FolderPopupWindow(Context context, FolderListAdapter folderListAdapter) {
        super(context);

        this.context = context;

        final View view = View.inflate(context, R.layout.folder_pop_layout, null);

        listView = (ListView) view.findViewById(R.id.pop_listview);
        listView.setAdapter(folderListAdapter);

        bg_view = (View) view.findViewById(R.id.bg_view);
        bg_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOutsideTouchable(false);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(0);
        setContentView(view);
        init();

    }


    private void init() {


    }


    public void setAnchorView(View anchorView) {
        this.anchorView = anchorView;
    }

    public void show() {
        if (anchorView != null && !isShowing()) {
            showAsDropDown(anchorView, 0, -POP_OFFSET);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(bg_view, "alpha", 0, 1);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", listView.getHeight(), 0);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(300);
            set.playTogether(alpha, translationY);
            set.setInterpolator(new AccelerateDecelerateInterpolator());
            set.start();
        }
    }


    @Override
    public void dismiss() {

        if (popupWindowHideListener != null) {
            popupWindowHideListener.hide();
        }
        ObjectAnimator alpha = ObjectAnimator.ofFloat(bg_view, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", 0, listView.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                FolderPopupWindow.super.dismiss();

                if (popupWindowHideListener != null) {
                    popupWindowHideListener.hideed();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();


    }

    public void setPopupWindowHideListener(PopupWindowHideListener popupWindowHideListener) {
        this.popupWindowHideListener = popupWindowHideListener;
    }

    public interface PopupWindowHideListener {
        void hide();

        void hideed();
    }


}
