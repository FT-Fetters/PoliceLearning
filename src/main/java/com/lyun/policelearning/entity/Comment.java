package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer parentId;
    private Date date;
    private String content;
    private String type;
    private Integer hostId;
}
