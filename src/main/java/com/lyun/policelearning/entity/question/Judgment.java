package com.lyun.policelearning.entity.question;

import lombok.Data;

@Data
public class Judgment {
    private int id;
    private String problem;
    private String option_true;
    private String option_false;
    private String answer;
}
