package com.lyun.policelearning.entity.course;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CourseContent {
    private int id;
    private int courseId;
    private String name;
    private String content;
    private Long planTime;
    private String beginTime;
    private String endTime;

}
