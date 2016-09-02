package cn.fxnn.hybphoto.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * **************************
 * Class:       PhotoFolderBean
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoFolderBean {

    private String id;
    private String path;
    private String name;
    private PhotoBean cur;
    private List<PhotoBean> photoBeens = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhotoBean getCur() {
        return cur;
    }

    public void setCur(PhotoBean cur) {
        this.cur = cur;
    }

    public List<PhotoBean> getPhotoBeens() {
        return photoBeens;
    }

    public void setPhotoBeens(List<PhotoBean> photoBeens) {
        this.photoBeens = photoBeens;
    }
}
