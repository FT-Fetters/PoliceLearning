package com.lyun.policelearning.controller.paper.model;

import lombok.Data;

import java.util.List;

/**
 * 用户提交试卷结构体
 */
@Data
public class PaperSubmitBody {
    //用户id
    private int user_id;
    //试卷id
    private int paper_id;
    //用户提交的答案
    private List<String> inputs;
}
