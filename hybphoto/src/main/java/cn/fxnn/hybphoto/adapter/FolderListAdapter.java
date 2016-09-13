package cn.fxnn.hybphoto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
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

    private int select_folder_index = 0;

    public FolderListAdapter(RequestManager glide, List<PhotoFolderBean> folderBeanList) {
        this.folderBeanList = folderBeanList;
        this.glide = glide;
    }


    public void setSelectFolderIndex(int i) {
        if (select_folder_index == i) {
            return;
        }
        select_folder_index = i;
        notifyDataSetChanged();
    }

    public int getSelectFolderIndex() {
        return select_folder_index;
    }


    @Override
    public int getCount() {
        return folderBeanList.size();
    }


    @Override
    public PhotoFolderBean getItem(int position) {
        return folderBeanList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return folderBeanList.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.folder_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(folderBeanList.get(position), position);

        return convertView;
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;
        public RadioButton rb_folder;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tvName = (TextView) rootView.findViewById(R.id.tv_dir_name);
            tvCount = (TextView) rootView.findViewById(R.id.tv_dir_count);
            rb_folder = (RadioButton) rootView.findViewById(R.id.rb_folder);
        }

        public void bindData(PhotoFolderBean photoFolderBean, int position) {
            glide.load(photoFolderBean.getCur().getPath())
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(ivCover);
            tvName.setText(photoFolderBean.getName());
            tvCount.setText(tvCount.getContext().getString(R.string.folder_image_count, photoFolderBean.getPhotoBeens().size()));

            if (select_folder_index == position) {
                rb_folder.setVisibility(View.VISIBLE);
                rb_folder.setChecked(true);
            } else {
                rb_folder.setChecked(false);
                rb_folder.setVisibility(View.INVISIBLE);
            }
        }
    }

}