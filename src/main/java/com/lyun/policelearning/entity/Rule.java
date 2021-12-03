package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;


@Data
public class Rule {
    private int id;
    private String title;
    private String content;
    private int view;
    private Date date;
}
