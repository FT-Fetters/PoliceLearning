package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Exam {
    private int id;
    private int user_id;
    private int paper_id;
    private Date date;
    private float score;
    private String input;
}
