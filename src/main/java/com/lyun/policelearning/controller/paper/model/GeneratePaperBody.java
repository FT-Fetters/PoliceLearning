package com.lyun.policelearning.controller.paper.model;

import com.lyun.policelearning.utils.page.PageRequest;
import lombok.Data;

@Data
public class GeneratePaperBody {
    private Integer j;
    private Integer m;
    private Integer s;
    private String title;
    private Integer score_j;
    private Integer score_s;
    private Integer score_m;
    private Integer uid;
    private Long time;
}
