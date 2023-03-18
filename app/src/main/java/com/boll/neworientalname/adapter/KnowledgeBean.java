package com.boll.neworientalname.adapter;

/**
 * created by zoro at 2023/3/10
 */
public class KnowledgeBean {

    private String knowledge;
    private boolean isCheck;

    public KnowledgeBean() {
    }

    public KnowledgeBean(String knowledge, boolean isCheck) {
        this.knowledge = knowledge;
        this.isCheck = isCheck;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

}
