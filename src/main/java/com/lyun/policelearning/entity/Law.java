package com.lyun.policelearning.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class Law {
    @ExcelIgnore
    private int id;
    @ExcelProperty("法律类型")
    private String lawtype;
    @ExcelProperty("法律名称")
    @ColumnWidth(20)
    private String title;
    @ExcelProperty("法律内容")
    @ColumnWidth(50)
    private String content;
    @ExcelProperty("释义阐明")
    @ColumnWidth(50)
    private String explaination;
    @ExcelProperty("罪名解析")
    @ColumnWidth(50)
    private String crime;
    @ExcelProperty("关键词")
    @ColumnWidth(50)
    private String keyword;
}
