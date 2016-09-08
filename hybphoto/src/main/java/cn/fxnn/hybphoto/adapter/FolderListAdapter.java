package cn.fxnn.hybphoto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;

/**
 * **************************
 * Class:       FolderListAdapter
 * Author:      fangx
 * Date:        16/9/6
 * Description:
 * ***************************
 */
public class FolderListAdapter extends BaseAdapter {


    private List<PhotoFolderBean> folderBeanList = new ArrayList<>();
    private RequestManager glide;

    public FolderListAdapter(RequestManager glide, List<PhotoFolderBean> folderBeanList) {
        this.folderBeanList = folderBeanList;
        this.glide = glide;
    }


    @Override public int getCount() {
        return folderBeanList.size();
    }


    @Override public PhotoFolderBean getItem(int position) {
        return folderBeanList.get(position);
    }


    @Override public long getItemId(int position) {
        return folderBeanList.get(position).hashCode();
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.__picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(folderBeanList.get(position));

        return convertView;
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tvName  = (TextView)  rootView.findViewById(R.id.tv_dir_name);
            tvCount = (TextView)  rootView.findViewById(R.id.tv_dir_count);
        }

        public void bindData(PhotoFolderBean photoFolderBean) {
            glide.load(photoFolderBean.getPath())
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(ivCover);
            tvName.setText(photoFolderBean.getName());
            tvCount.setText(tvCount.getContext().getString(R.string.folder_image_count, photoFolderBean.getPhotoBeens().size()));
        }
    }

}