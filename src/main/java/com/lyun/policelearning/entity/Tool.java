package com.lyun.policelearning.entity;

import lombok.Data;

/**
 * 执法工具箱
 */
@Data
public class Tool {
    private Integer id;
    private String title;
    private String content;
    private String date;
    private int type;
    private String typeName;
}
