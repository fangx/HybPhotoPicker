package cn.fxnn.hybphoto.listener;

import cn.fxnn.hybphoto.bean.PhotoBean;

/**
 * **************************
 * Class:       PhotoSelect
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public interface PhotoSelect {

    boolean isSelected(PhotoBean photoBean);

    void toggleSelection(PhotoBean photoBean);

    void clearSelection();

    int getSelectedItemCount();

}
