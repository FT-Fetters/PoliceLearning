package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class CourseQuestion {
    private long id;
    private long contentId;
    private String question;
    private boolean answer;
}

