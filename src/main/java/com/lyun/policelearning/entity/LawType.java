package com.lyun.policelearning.entity;

import lombok.Data;

//建立法律类型与具体法律的关系
@Data
public class LawType {
    private int id;
    private String lawtype;
    private String title;
}
