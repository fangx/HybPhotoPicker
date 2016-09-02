package cn.fxnn.hybphoto.helper;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;

import static android.provider.BaseColumns._ID;

/**
 * **************************
 * Class:       PhotoScanHelper
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoScanHelper {


    public interface PhotoScanListener {
        void scanComplete(List<PhotoFolderBean> folderBeanList);
    }

    public static void scanPhotos(FragmentActivity activity, PhotoScanListener photoScanListener) {
        activity.getSupportLoaderManager().initLoader(0, null, new PhotoFolderScanCallbacks(activity, photoScanListener));
    }


    private static class PhotoFolderScanCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {


        private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
                MediaStore.Images.Media._ID,            //id
                MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称
                MediaStore.Images.Media.DATA,           //图片的真实路径
                MediaStore.Images.Media.SIZE,           //图片的大小，long型
                MediaStore.Images.Media.WIDTH,          //图片的宽度，int型
                MediaStore.Images.Media.HEIGHT,         //图片的高度，int型
                MediaStore.Images.Media.MIME_TYPE,      //图片的类型
                MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型

        private WeakReference<Context> context;
        private PhotoScanListener photoScanListener;

        public PhotoFolderScanCallbacks(Context context, PhotoScanListener photoScanListener) {
            this.context = new WeakReference<>(context);
            this.photoScanListener = photoScanListener;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(context.get(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;

            ArrayList<PhotoFolderBean> photoFolderBeans = new ArrayList<>();

            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                long size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int width = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                int height = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                String mimetype = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                long creat_time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]));


                PhotoBean photoBean = new PhotoBean();
                photoBean.setId(id);
                photoBean.setName(name);
                photoBean.setPath(path);
                photoBean.setSize(size);
                photoBean.setWidth(width);
                photoBean.setHeight(height);
                photoBean.setMimeType(mimetype);
                photoBean.setCreat_time(creat_time);


            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    }


}
