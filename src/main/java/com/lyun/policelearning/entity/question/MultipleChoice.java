package com.lyun.policelearning.entity.question;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class MultipleChoice {
    private int id;
    @ExcelProperty("问题")
    private String problem;
    @ExcelProperty("选项A")
    private String option_a;
    @ExcelProperty("选项B")
    private String option_b;
    @ExcelProperty("选项C")
    private String option_c;
    @ExcelProperty("选项D")
    private String option_d;
    @ExcelProperty("答案")
    private String answer;
}
