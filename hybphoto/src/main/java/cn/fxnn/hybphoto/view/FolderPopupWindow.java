package cn.fxnn.hybphoto.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.internal.content.NativeLibraryHelper;

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

    //列表高度
    private int list_height = 0;

    private FolderListAdapter listAdapter;

    public FolderPopupWindow(Context context, FolderListAdapter folderListAdapter) {
        super(context);

        this.context = context;
        this.listAdapter = folderListAdapter;

        final View view = View.inflate(context, R.layout.folder_pop_layout, null);

        listView = (ListView) view.findViewById(R.id.pop_listview);
        listView.setAdapter(folderListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listAdapter.setSelectFolderIndex(position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                        if (popupWindowHideListener != null) {
                            popupWindowHideListener.changeFolder(position);
                        }
                    }
                }, 100);
            }
        });


        bg_view = view.findViewById(R.id.bg_view);
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

    }


    public void setAnchorView(View anchorView) {
        this.anchorView = anchorView;
    }

    public void show() {
        if (anchorView != null && !isShowing()) {
            showAsDropDown(anchorView, 0, -POP_OFFSET);

            if (list_height != 0) {
                showAnim();
            } else {
                listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        list_height = listView.getHeight();
                        showAnim();
                    }
                });
            }
        }
    }


    public void showAnim() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(bg_view, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", list_height, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    @Override
    public void dismiss() {

        if (popupWindowHideListener != null) {
            popupWindowHideListener.hide();
        }
        ObjectAnimator alpha = ObjectAnimator.ofFloat(bg_view, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", 0, list_height);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
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

        void changeFolder(int position);
    }


}
