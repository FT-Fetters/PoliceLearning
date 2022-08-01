package com.lyun.policelearning.controller.paper.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GradesExportBody {
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("名称")
    private String nickname;
    @ExcelProperty("日期")
    private Date date;
    @ExcelProperty("试卷标题")
    private String title;
    @ExcelProperty("分数")
    private float score;

}
