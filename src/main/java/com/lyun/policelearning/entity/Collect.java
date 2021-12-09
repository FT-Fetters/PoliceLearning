package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Collect {
    private int id;
    private int type;
    private int articleId;
    private Date date;
}
