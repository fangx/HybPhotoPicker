package cn.fxnn.hybphoto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import java.io.File;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;
import cn.fxnn.hybphoto.listener.PhotoCheckListener;
import cn.fxnn.hybphoto.listener.PhotoClickListener;
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

    private RequestManager glideRequestManager;

    private View.OnClickListener cameraClickListener = null;
    private PhotoCheckListener photoCheckListener = null;
    private PhotoClickListener photoClickListener = null;

    public PhotoFlowAdapter(Context context, List<PhotoFolderBean> photoFolderBeanList, RequestManager glideRequestManager) {
        this.photoFolderBeens = photoFolderBeanList;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / 3;
        this.glideRequestManager = glideRequestManager;
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
                    if (cameraClickListener != null) {
                        cameraClickListener.onClick(view);
                    }
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

            glideRequestManager
                    .load(new File(photoBean.getPath()))
                    .centerCrop()
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .override(imageSize, imageSize)
                    .placeholder(R.drawable.default_200)
                    .error(R.drawable.default_200)
                    .into(holder.ivPhoto);


            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


            final boolean isChecked = isSelected(photoBean);

            if (isChecked) {
                holder.mask.setVisibility(View.VISIBLE);
                holder.cbPhoto.setSelected(true);
            } else {
                holder.mask.setVisibility(View.GONE);
                holder.cbPhoto.setSelected(false);
            }

            holder.cbPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;
                    if (photoCheckListener != null) {
                        isEnable = photoCheckListener.photoCheck(pos, isChecked, photoBean,
                                getSelectedPhotos().size());
                    }
                    if (isEnable) {
                        if (isSelected(photoBean)) {
                            holder.mask.setVisibility(View.VISIBLE);
                            holder.cbPhoto.setSelected(true);
                        } else {
                            holder.mask.setVisibility(View.GONE);
                            holder.cbPhoto.setSelected(false);
                        }
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
        public ImageView cbPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mask = itemView.findViewById(R.id.photo_mask);
            cbPhoto = (ImageView) itemView.findViewById(R.id.ck_photo);
        }
    }


    public void setShowCamera(boolean show_camera) {
        this.show_camera = show_camera;
    }

    public boolean showCamera() {
        return (show_camera && currentDirectoryIndex == MediaStoreHelper.INDEX_ALL_PHOTOS);
    }


    public View.OnClickListener getCameraClickListener() {
        return cameraClickListener;
    }

    public void setCameraClickListener(View.OnClickListener cameraClickListener) {
        this.cameraClickListener = cameraClickListener;
    }

    public PhotoCheckListener getPhotoCheckListener() {
        return photoCheckListener;
    }

    public void setPhotoCheckListener(PhotoCheckListener photoCheckListener) {
        this.photoCheckListener = photoCheckListener;
    }

    public PhotoClickListener getPhotoClickListener() {
        return photoClickListener;
    }

    public void setPhotoClickListener(PhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }
}
