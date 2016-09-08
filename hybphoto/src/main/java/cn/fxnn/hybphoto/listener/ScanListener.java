package cn.fxnn.hybphoto.listener;

import java.util.List;

import cn.fxnn.hybphoto.bean.PhotoFolderBean;

/**
 * **************************
 * Class:       ScanListener
 * Author:      fangx
 * Date:        16/9/6
 * Description:
 * ***************************
 */
public interface ScanListener {

    void scanSuccess(List<PhotoFolderBean> photoFolderBeanList);

}
