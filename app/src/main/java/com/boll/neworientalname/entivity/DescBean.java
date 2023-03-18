package com.boll.neworientalname.entivity;

/**
 * created by zoro at 2023/3/10
 */
public class DescBean {

    private String name;
    private String imgPath;

    public DescBean() {
    }

    public DescBean(String name, String imgPath) {
        this.name = name;
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
