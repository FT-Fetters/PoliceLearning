package com.lyun.policelearning.entity;


import lombok.Data;

import java.sql.Date;
@Data
public class ErrorBook {
    private int userId;
    private int type;
    private int questionId;
    private Date date;
}
