package com.lyun.policelearning.entity.question;


import lombok.Data;

@Data
public class MultipleChoice {
    private int id;
    private String problem;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String answer;
}
