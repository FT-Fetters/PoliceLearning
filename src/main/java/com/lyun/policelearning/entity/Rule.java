package com.lyun.policelearning.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;


@Data
public class Rule {
    @ExcelIgnore
    private int id;
    @ExcelProperty("标题")
    @ColumnWidth(20)
    private String title;
    @ExcelProperty("内容")
    @ColumnWidth(50)
    private String content;
    @ExcelIgnore
    private int view;
    @ExcelProperty("日期")
    @ColumnWidth(20)
    private String date;
}
