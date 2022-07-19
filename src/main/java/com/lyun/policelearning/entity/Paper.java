package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Paper {
    private Integer id;
    private Integer createUser;
    private Date date;
    private String title;
    private boolean enable;
}
