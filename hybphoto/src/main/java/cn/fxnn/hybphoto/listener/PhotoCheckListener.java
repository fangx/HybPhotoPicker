package cn.fxnn.hybphoto.listener;

import cn.fxnn.hybphoto.bean.PhotoBean;

/**
 * **************************
 * Class:       PhotoCheckListener
 * Author:      fangx
 * Date:        16/9/2
 * Description:
 * ***************************
 */
public interface PhotoCheckListener {

    boolean photoCheck(int position, boolean isCheck, PhotoBean photoBean, int selectedCount);

}
