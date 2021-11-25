package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class Law {
    private int id;
    private String lawtype;
    private String title;
    private String content;
    private String explaination;
    private String crime;
    private String keyword;
}
