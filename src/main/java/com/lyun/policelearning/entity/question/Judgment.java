package com.lyun.policelearning.entity.question;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Judgment {
    private int id;

    @ExcelProperty("题目")
    private String problem;

    @ExcelProperty("正确选项")
    private String option_true;

    @ExcelProperty("错误选项")
    private String option_false;

    @ExcelProperty("答案")
    private String answer;
}
