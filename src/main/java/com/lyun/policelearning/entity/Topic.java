package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class Topic {
    private int id;
    private int uid;
    private String realName;
    private String title;
    private String content;
    private String date;
    private String picture;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
