package cn.fxnn.hybphoto.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.listener.PhotoCheckListener;
import cn.fxnn.hybphoto.ui.fragment.PhotoFlowFragment;

import static android.widget.Toast.LENGTH_LONG;

/**
 * **************************
 * Class:       PhotoFlowActivity
 * Author:      fangx
 * Date:        16/9/2
 * Description:
 * ***************************
 */
public class PhotoFlowActivity extends BaseAppCompatActivity {

    private static final String PF_FRAGMENT_TAG = "pf_fragment";

    private PhotoFlowFragment photoFlowFragment;

    private int maxCount = 9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pf_activity_layout);

        init();
    }


    private void init() {

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.top_bar);
//        setSupportActionBar(mToolbar);
//        setTitle("图片选择");
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            actionBar.setElevation(25);
//        }

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

                if (maxCount <= 1) {
                    List<String> photos = photoFlowFragment.getPhotoFlowAdapter().getSelectedPhotos();
                    if (!photos.contains(photoBean.getPath())) {
                        photos.clear();
                        photoFlowFragment.getPhotoFlowAdapter().notifyDataSetChanged();
                    }
                    return true;
                }

                if (total > maxCount) {
                    Toast.makeText(PhotoFlowActivity.this, getString(R.string.photo_max_count_tips, maxCount),
                            LENGTH_LONG).show();
                    return false;
                }
//                menuDoneItem.setTitle(getString(R.string.__picker_done_with_count, total, maxCount));
                return true;
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
