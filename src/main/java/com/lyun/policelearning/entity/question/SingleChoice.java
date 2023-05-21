package com.lyun.policelearning.entity.question;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SingleChoice {
    @ExcelIgnore
    private int id;
    @ExcelProperty("问题")
    private String problem;
    @ExcelProperty("选项A")
    private String optionA;
    @ExcelProperty("选项B")
    private String optionB;
    @ExcelProperty("选项C")
    private String optionC;
    @ExcelProperty("选项D")
    private String optionD;
    @ExcelProperty("答案")
    private String answer;
}
