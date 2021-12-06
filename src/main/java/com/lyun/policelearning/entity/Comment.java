package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Comment {
    private int id;
    private int userId;
    private int parentId;
    private Date date;
    private String content;
    private String type;
    private int hostId;
}
