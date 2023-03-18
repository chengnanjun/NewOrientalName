package com.boll.neworientalname.response;

import java.io.Serializable;
import java.util.List;

/**
 * created by zoro at 2023/3/9
 * 一层子项数据结构
 */
public class OneChildrenBean implements Serializable {

    private String name;
    private List<VideoBean> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoBean> getChildren() {
        return children;
    }

    public void setChildren(List<VideoBean> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ChildrenBean{" +
                "name='" + name + '\'' +
                ", children=" + children +
                '}';
    }

}