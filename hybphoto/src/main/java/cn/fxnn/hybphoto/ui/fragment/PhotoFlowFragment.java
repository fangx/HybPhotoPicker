package cn.fxnn.hybphoto.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.adapter.PhotoFlowAdapter;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;
import cn.fxnn.hybphoto.helper.PhotoScanHelper;
import cn.fxnn.hybphoto.listener.ScanListener;
import cn.fxnn.hybphoto.utils.Constants;

/**
 * **************************
 * Class:       PhotoFlowFragment
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoFlowFragment extends Fragment {

    private final static String SHOW_CAMERA = "show_camera";
    private final static String SELECTED_PHOTOS = "selected_photos";

    private RequestManager glideRequestManager;

    private PhotoFlowAdapter photoFlowAdapter;

    List<PhotoFolderBean> photoFolderBeanList = new ArrayList<>();

    ScanListener scanListener;

    public static PhotoFlowFragment newInstance(boolean showCamera, ArrayList<String> selectedPhotos) {
        Bundle args = new Bundle();
        args.putBoolean(SHOW_CAMERA, showCamera);
        args.putStringArrayList(SELECTED_PHOTOS, selectedPhotos);
        PhotoFlowFragment fragment = new PhotoFlowFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        glideRequestManager = Glide.with(this);
        photoFlowAdapter = new PhotoFlowAdapter(this.getActivity().getApplicationContext(), photoFolderBeanList, glideRequestManager);
        photoFlowAdapter.setShowCamera(false);


        PhotoScanHelper.scanPhotos(this.getActivity(), new PhotoScanHelper.PhotoScanListener() {
            @Override
            public void scanComplete(List<PhotoFolderBean> folderBeanList) {
                photoFolderBeanList.clear();
                photoFolderBeanList.addAll(folderBeanList);
                if (scanListener != null) {
                    scanListener.scanSuccess(folderBeanList);
                }
                photoFlowAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.pf_fragment_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photo_flow);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Constants.COLUMN, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoFlowAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }


    public PhotoFlowAdapter getPhotoFlowAdapter() {
        return photoFlowAdapter;
    }

    public void setPhotoFlowAdapter(PhotoFlowAdapter photoFlowAdapter) {
        this.photoFlowAdapter = photoFlowAdapter;
    }

    public List<PhotoFolderBean> getPhotoFolderBeanList() {
        return photoFolderBeanList;
    }

    public void setPhotoFolderBeanList(List<PhotoFolderBean> photoFolderBeanList) {
        this.photoFolderBeanList = photoFolderBeanList;
    }

    public RequestManager getGlideRequestManager() {
        return glideRequestManager;
    }

    public void setGlideRequestManager(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    public ScanListener getScanListener() {
        return scanListener;
    }

    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }
}
