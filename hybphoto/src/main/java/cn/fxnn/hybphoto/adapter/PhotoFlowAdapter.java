package cn.fxnn.hybphoto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;
import cn.fxnn.hybphoto.utils.MediaStoreHelper;

/**
 * **************************
 * Class:       PhotoFlowAdapter
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoFlowAdapter extends PickerAdapter<PhotoFlowAdapter.PhotoViewHolder> {

    public final static int ITEM_TYPE_PHOTO = 123456;

    public final static int ITEM_TYPE_CAMERA = 123457;

    private int imageSize;

    private LayoutInflater inflater;

    //是否需要显示相机
    private boolean show_camera = false;


    public PhotoFlowAdapter(Context context, List<PhotoFolderBean> photoFolderBeanList) {
        this.photoFolderBeens = photoFolderBeanList;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / 3;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.adapter_photo_item, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);
        if (viewType == ITEM_TYPE_CAMERA) {
            holder.cbPhoto.setVisibility(View.GONE);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (onCameraClickListener != null) {
//                        onCameraClickListener.onClick(view);
//                    }
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {


        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {

            List<PhotoBean> photoBeanList = getCurrentPhotos();
            final PhotoBean photoBean;

            if (showCamera()) {
                photoBean = photoBeanList.get(position - 1);
            } else {
                photoBean = photoBeanList.get(position);
            }


            final boolean isChecked = isSelected(photoBean);

            holder.cbPhoto.setChecked(isChecked);
            holder.ivPhoto.setSelected(isChecked);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.cbPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;
                    //                    if (onItemCheckListener != null) {
//                        isEnable = onItemCheckListener.OnItemCheck(pos, photoBean, isChecked,
//                                getSelectedPhotos().size());
//                    }
                    if (isEnable) {
                        toggleSelection(photoBean);
                        notifyItemChanged(pos);
                    }
                }
            });


        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_camera_grey);
        }


    }

    @Override
    public int getItemCount() {
        int photosCount =
                photoFolderBeens.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;
        public View mask;
        public CheckBox cbPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mask = itemView.findViewById(R.id.photo_mask);
            cbPhoto = (CheckBox) itemView.findViewById(R.id.ck_photo);
        }
    }


    public void setShowCamera(boolean show_camera) {
        this.show_camera = show_camera;
    }

    public boolean showCamera() {
        return (show_camera && currentDirectoryIndex == MediaStoreHelper.INDEX_ALL_PHOTOS);
    }

}
