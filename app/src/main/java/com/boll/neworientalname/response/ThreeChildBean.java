package com.boll.neworientalname.response;

import java.io.Serializable;
import java.util.List;

/**
 * created by zoro at 2023/3/9
 * 三层子项数据结构
 */
public class ThreeChildBean implements Serializable {

    private String zj;

    private List<ChildrenBean> children;

    public String getZj() {
        return zj;
    }

    public void setZj(String zj) {
        this.zj = zj;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class ChildrenBean implements Serializable {

        private int grade;
        private String name;
        private String description;
        private String pict_url;
        private List<OneChildrenBean> children;

        public int getGrade(){
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPict_url() {
            return pict_url;
        }

        public void setPict_url(String pict_url) {
            this.pict_url = pict_url;
        }

        public List<OneChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<OneChildrenBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "ChildrenBean{" +
                    "grade=" + grade +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", pict_url='" + pict_url + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ThreeChildBean{" +
                "zj='" + zj + '\'' +
                ", children=" + children +
                '}';
    }

}
