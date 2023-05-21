package com.lyun.policelearning.entity.question;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Judgment {
    @ExcelIgnore
    private int id;

    @ExcelProperty("题目")
    private String problem;

    @ExcelProperty("正确选项")
    private String optionTrue;

    @ExcelProperty("错误选项")
    private String optionFalse;

    @ExcelProperty("答案")
    private String answer;
}
