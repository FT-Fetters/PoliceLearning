package com.lyun.policelearning.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

//建立法律类型与具体法律的关系
@Data
public class LawType {
    @ExcelIgnore
    private int id;
    @ExcelProperty("法律类型")
    @ColumnWidth(20)
    private String lawtype;
    @ExcelIgnore
    private String title;
}
