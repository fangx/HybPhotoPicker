package cn.fxnn.hybphoto.helper;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.fxnn.hybphoto.R;
import cn.fxnn.hybphoto.bean.PhotoFolderBean;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * **************************
 * Class:       PhotoDataHelper
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoDataHelper {

    public final static int ALL_PHOTOS = 0;


    public static void getPhotos(FragmentActivity activity, Bundle args, PhotosScanCallback photosScanCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoFolderScanCallbacks(activity, photosScanCallback));
    }

    private static class PhotoFolderScanCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private PhotosScanCallback resultCallback;

        public PhotoFolderScanCallbacks(Context context, PhotosScanCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(PhotoPicker.EXTRA_SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;
            List<PhotoFolderBean> photoFolderBeens = new ArrayList<>();
            PhotoFolderBean allFolder = new PhotoFolderBean();
            allFolder.setName(context.get().getString(R.string.__picker_all_image));
            allFolder.setId("ALL");

            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

                if (size < 1) continue;

                PhotoFolderBean photoFolderBean = new PhotoFolderBean();
                photoFolderBean.setId(bucketId);
                photoFolderBean.setName(name);

                if (!photoFolderBeens.contains(photoFolderBean)) {
                    photoFolderBean.setPath(path);
                    photoFolderBean.addPhoto(imageId, path);
                    photoFolderBean.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    photoFolderBeens.add(photoFolderBean);
                } else {
                    photoFolderBeens.get(photoFolderBeens.indexOf(photoFolderBean)).addPhoto(imageId, path);
                }

                allFolder.addPhoto(imageId, path);
            }
            if (allFolder.getPhotoPaths().size() > 0) {
                allFolder.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
            }
            photoFolderBeens.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onScanCallback(photoFolderBeens);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosScanCallback {
        void onScanCallback(List<PhotoFolderBean> folderBeanList);
    }


}
