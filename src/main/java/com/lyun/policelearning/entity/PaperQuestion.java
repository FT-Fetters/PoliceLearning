package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class PaperQuestion {
    private Integer id;
    private Integer paper_id;
    private Integer question_id;
    private Character type;
    private int index;
}
