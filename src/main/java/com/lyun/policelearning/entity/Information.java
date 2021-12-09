package com.lyun.policelearning.entity;
import lombok.Data;

import java.sql.Date;


@Data
public class Information {
    private int id;
    private String title;
    private String picture;
    private String content;
    private Date date;
    private int view;
    private int istop;
}
