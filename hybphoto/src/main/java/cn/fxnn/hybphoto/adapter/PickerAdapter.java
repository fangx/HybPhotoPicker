package cn.fxnn.hybphoto.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;
import cn.fxnn.hybphoto.listener.PhotoSelect;

/**
 * **************************
 * Class:       PickerAdapter
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public abstract class PickerAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> implements PhotoSelect {

    private static final String TAG = PickerAdapter.class.getSimpleName();

    protected List<PhotoFolderBean> photoFolderBeens;
    protected List<String> selectedPhotos;

    public int currentDirectoryIndex = 0;


    public PickerAdapter() {
        photoFolderBeens = new ArrayList<>();
        selectedPhotos = new ArrayList<>();
    }


    @Override
    public boolean isSelected(PhotoBean photoBean) {
        return getSelectedPhotos().contains(photoBean.getPath());
    }


    //设置图片选中状态
    @Override
    public void toggleSelection(PhotoBean photoBean) {
        if (selectedPhotos.contains(photoBean.getPath())) {
            selectedPhotos.remove(photoBean.getPath());
        } else {
            selectedPhotos.add(photoBean.getPath());
        }
    }


    @Override
    public void clearSelection() {
        selectedPhotos.clear();
    }


    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }


    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }


    public List<PhotoBean> getCurrentPhotos() {
        return photoFolderBeens.get(currentDirectoryIndex).getPhotoBeens();
    }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (PhotoBean photoBean : getCurrentPhotos()) {
            currentPhotoPaths.add(photoBean.getPath());
        }
        return currentPhotoPaths;
    }


    public List<String> getSelectedPhotos() {
        return selectedPhotos;
    }

}