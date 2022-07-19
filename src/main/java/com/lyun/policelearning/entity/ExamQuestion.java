package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class ExamQuestion {
    private int id;
    private int exam_id;
    private int question_id;
    private char type;
    private String input;
}
