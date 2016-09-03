package cn.fxnn.hybphoto.helper;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.bean.PhotoBean;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;

/**
 * **************************
 * Class:       PhotoScanHelper
 * Author:      fangx
 * Date:        16/9/1
 * Description: 图片检索辅助类
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


        private final String[] IMAGE_PROJECTION = {           //查询图片需要的数据列
                MediaStore.Images.Media._ID,                  //id
                MediaStore.Images.Media.BUCKET_ID,            //文件目录id
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  //文件目录名
                MediaStore.Images.Media.DISPLAY_NAME,         //图片的显示名称
                MediaStore.Images.Media.DATA,                 //图片的真实路径
                MediaStore.Images.Media.SIZE,                 //图片的大小，long型
                MediaStore.Images.Media.WIDTH,                //图片的宽度，int型
                MediaStore.Images.Media.HEIGHT,               //图片的高度，int型
                MediaStore.Images.Media.MIME_TYPE,            //图片的类型
                MediaStore.Images.Media.DATE_ADDED};          //图片被添加的时间，long型

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
            ArrayList<PhotoBean> allPhotos = new ArrayList<>();

            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String bucketId = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                String bucketName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                long size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                int width = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                int height = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]));
                String mimetype = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[8]));
                long creat_time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[9]));


                PhotoBean photoBean = new PhotoBean();
                photoBean.setId(id);
                photoBean.setName(name);
                photoBean.setPath(path);
                photoBean.setSize(size);
                photoBean.setWidth(width);
                photoBean.setHeight(height);
                photoBean.setMimeType(mimetype);
                photoBean.setCreat_time(creat_time);

                allPhotos.add(photoBean);

                File photoFile = new File(path);
                File photoFolderFile = photoFile.getParentFile();

                PhotoFolderBean photoFolderBean = new PhotoFolderBean();
                photoFolderBean.setId(bucketId);
                photoFolderBean.setName(bucketName);
                photoFolderBean.setPath(photoFolderFile.getAbsolutePath());

                if (photoFolderBeans.contains(photoFolderBean)) {
                    photoFolderBeans.get(photoFolderBeans.indexOf(photoFolderBean)).getPhotoBeens().add(photoBean);
                } else {
                    ArrayList<PhotoBean> photos = new ArrayList<>();
                    photos.add(photoBean);
                    photoFolderBean.setCur(photoBean);
                    photoFolderBean.setPhotoBeens(photos);
                    photoFolderBeans.add(photoFolderBean);
                }

                //防止没有图片报异常
                if (data.getCount() > 0) {
                    //构造所有图片的集合
                    PhotoFolderBean allPhotoFolderBean = new PhotoFolderBean();
                    allPhotoFolderBean.setName("全部图片");
                    allPhotoFolderBean.setPath("/");
                    allPhotoFolderBean.setCur(allPhotos.get(0));
                    allPhotoFolderBean.setPhotoBeens(allPhotos);
                    allPhotoFolderBean.setId("");
                    photoFolderBeans.add(0, allPhotoFolderBean);
                }


            }

            photoScanListener.scanComplete(photoFolderBeans);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    }


}
