package cn.fxnn.hybphoto.ui.activity;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.adapter.FolderListAdapter;
import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;
import cn.fxnn.hybphoto.listener.PhotoCheckListener;
import cn.fxnn.hybphoto.listener.ScanListener;
import cn.fxnn.hybphoto.ui.fragment.PhotoFlowFragment;
import cn.fxnn.hybphoto.utils.BaseUtil;
import cn.fxnn.hybphoto.utils.TypefaceUtils;
import cn.fxnn.hybphoto.view.FolderPopupWindow;

import static android.widget.Toast.LENGTH_LONG;

/**
 * **************************
 * Class:       PhotoFlowActivity
 * Author:      fangx
 * Date:        16/9/2
 * Description:
 * ***************************
 */
public class PhotoFlowActivity extends BaseAppCompatActivity implements View.OnClickListener, FolderPopupWindow.PopupWindowHideListener {

    private static final String PF_FRAGMENT_TAG = "pf_fragment";
    private PhotoFlowFragment photoFlowFragment;
    private RequestManager glideRequestManager;
    private int maxCount = 9;
    public static int FOLDER_COUNT_MAX = 5;

    //状态栏高度
    private int statusBarHeight = 0;
    private FolderPopupWindow listPopupWindow;
    private FolderListAdapter folderListAdapter;
    private List<PhotoFolderBean> photoFolderBeanList;

    //是否正在隐藏folder
    private boolean hiding = false;

    //完成按钮
    private Button btn_ok;
    //图片目录列表
    private Button btn_dir;
    //预览按钮
    private Button btn_preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pf_activity_layout);

        init();
    }


    private void init() {

        statusBarHeight = getStatusBarHeight();

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setEnabled(false);

        btn_dir = (Button) findViewById(R.id.btn_dir);
        Drawable drawable = getResources().getDrawable(R.drawable.allphoto_tv_right);
        drawable.setBounds(BaseUtil.dip2px(this.getApplicationContext(), 2), BaseUtil.dip2px(this.getApplicationContext(), 2), BaseUtil.dip2px(this.getApplicationContext(), 10), BaseUtil.dip2px(this.getApplicationContext(), 10));
        btn_dir.setCompoundDrawables(null, null, drawable, null);//只放左边
        btn_dir.setOnClickListener(this);

        btn_preview = (Button) findViewById(R.id.btn_preview);
        btn_preview.setEnabled(false);
        btn_preview.setOnClickListener(this);

        photoFlowFragment = (PhotoFlowFragment) getSupportFragmentManager().findFragmentByTag(PF_FRAGMENT_TAG);

        if (photoFlowFragment == null) {
            photoFlowFragment = PhotoFlowFragment
                    .newInstance(false, new ArrayList<String>());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_pf_fragment, photoFlowFragment, PF_FRAGMENT_TAG)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        photoFlowFragment.setScanListener(new ScanListener() {
            @Override
            public void scanSuccess(List<PhotoFolderBean> photoFolderBeanList) {
                initPopView(photoFolderBeanList);
            }
        });

        photoFlowFragment.getPhotoFlowAdapter().setPhotoCheckListener(new PhotoCheckListener() {
            @Override
            public boolean photoCheck(int position, boolean isCheck, PhotoBean photoBean, int selectedCount) {

                int total = selectedCount + (isCheck ? -1 : 1);

                btn_ok.setEnabled(total > 0);
                btn_preview.setEnabled(total > 0);

                if (maxCount <= 1) {
                    List<String> photos = photoFlowFragment.getPhotoFlowAdapter().getSelectedPhotos();
                    if (!photos.contains(photoBean.getPath())) {
                        photos.clear();
                        photoFlowFragment.getPhotoFlowAdapter().notifyDataSetChanged();
                    }
                    return true;
                }

                if (total > maxCount) {
                    Toast.makeText(PhotoFlowActivity.this.getApplicationContext(), getString(R.string.photo_max_count_tips, maxCount),
                            LENGTH_LONG).show();
                    return false;
                }

                if (total > 0) {
                    btn_ok.setText(getString(R.string.photo_picker_ok, total, maxCount));
                    btn_preview.setText(getString(R.string.photo_picker_preview, total));
                } else {
                    btn_ok.setText(getString(R.string.ok));
                    btn_preview.setText(getString(R.string.preview));
                }

                return true;
            }
        });


    }


    //初始化图片文件夹选择弹框
    private void initPopView(final List<PhotoFolderBean> photoFolderBeanList) {

        glideRequestManager = photoFlowFragment.getGlideRequestManager();
        folderListAdapter = new FolderListAdapter(glideRequestManager, photoFolderBeanList);

        listPopupWindow = new FolderPopupWindow(this.getApplicationContext(), folderListAdapter);
        listPopupWindow.setAnchorView(btn_dir);
        listPopupWindow.setPopupWindowHideListener(this);

    }


//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.btn_dir) {
//            if (listPopupWindow != null) {
//                if (listPopupWindow.isShowing() && !hiding) {
//                    listPopupWindow.dismiss();
//                } else {
//                    if (!all_folder_click && !hiding) {
//                        adjustHeight();
//                        listPopupWindow.show();
//                        all_folder_click = true;
//                    }
//                }
//            }
//
//        }
//    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_dir) {
            if (hiding) {
                return;
            }
            if (listPopupWindow != null) {
                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else {
                    adjustHeight();
                    listPopupWindow.show();
                }
            }

        }
    }


    public void adjustHeight() {
        if (folderListAdapter == null || listPopupWindow == null) return;
        listPopupWindow.setHeight(BaseUtil.getHeight(this) - 2 * BaseUtil.dp2px(getApplicationContext(), 48) - statusBarHeight + FolderPopupWindow.POP_OFFSET);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void hide() {
        hiding = true;
    }

    @Override
    public void hideed() {
        hiding = false;
    }

    @Override
    public void changeFolder(int position) {
        photoFlowFragment.getPhotoFlowAdapter().setCurrentDirectoryIndex(position);
        photoFlowFragment.getPhotoFlowAdapter().notifyDataSetChanged();
    }
}
