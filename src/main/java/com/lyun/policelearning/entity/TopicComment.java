package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class TopicComment {
    private int id;
    private int uid;
    private String realName;
    private int tid;
    private String replay;
    private boolean isAccept;
    private String date;
}
