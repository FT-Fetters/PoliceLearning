package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class SchedulePaper {
    private Long id;
    private String cron;
    private int j;
    private int s;
    private int m;
    private String title;
}
