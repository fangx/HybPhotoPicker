package cn.fxnn.hybphoto.ui.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.listener.PhotoCheckListener;
import cn.fxnn.hybphoto.ui.fragment.PhotoFlowFragment;
import cn.fxnn.hybphoto.utils.BaseUtil;
import cn.fxnn.hybphoto.utils.TypefaceUtils;

import static android.widget.Toast.LENGTH_LONG;

/**
 * **************************
 * Class:       PhotoFlowActivity
 * Author:      fangx
 * Date:        16/9/2
 * Description:
 * ***************************
 */
public class PhotoFlowActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String PF_FRAGMENT_TAG = "pf_fragment";
    private PhotoFlowFragment photoFlowFragment;
    private int maxCount = 9;


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
        TypefaceUtils.TYPEFACE.setFontNY(btn_preview);

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

                btn_ok.setText(getString(R.string.photo_picker_ok, total, maxCount));
                btn_preview.setText(getString(R.string.photo_picker_preview, total));

                return true;
            }
        });

    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
