package com.lyun.policelearning.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class UserTemplate {
    @ExcelIgnore
    private int id;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("电话")
    @ColumnWidth(20)
    private String phone;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("部门")
    private String dept;
}
