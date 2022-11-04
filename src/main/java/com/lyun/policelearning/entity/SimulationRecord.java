package com.lyun.policelearning.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class SimulationRecord {
    private Integer id;
    private Integer user_id;
    private Date date;
    private Time time;
    private Float score;
}
