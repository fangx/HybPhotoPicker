package cn.fxnn.hybphoto.bean;

/**
 * **************************
 * Class:       PhotoBean
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoBean {
    
    private int id;
    private String name;       //图片的名字
    private String path;       //图片的路径
    private long size;         //图片的大小
    private int width;         //图片的宽度
    private int height;        //图片的高度
    private String mimeType;   //图片的类型
    private long creat_time;      //图片的创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(long creat_time) {
        this.creat_time = creat_time;
    }
}
