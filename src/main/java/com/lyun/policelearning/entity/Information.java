package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;


@Data
public class Information {
    private int id;
    private String title;
    private Date date;
    private String content;
    private String other;
}
