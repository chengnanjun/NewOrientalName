package com.boll.neworientalname.response;

/**
 * created by zoro at 2023/3/9
 * 主类目
 */
public class CategoryBean {

    private String name;
    private String ver;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "name='" + name + '\'' +
                ", ver='" + ver + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
