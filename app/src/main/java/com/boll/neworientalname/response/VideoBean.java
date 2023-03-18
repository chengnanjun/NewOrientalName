package com.boll.neworientalname.response;

import java.io.Serializable;

/**
 * created by zoro at 2023/3/9
 */
public class VideoBean implements Serializable {

    private String name;
    private Integer num;
    private String desc;
    private String time;
    private String url;
    private String pict_url;

    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", pict_url='" + pict_url + '\'' +
                '}';
    }


}
