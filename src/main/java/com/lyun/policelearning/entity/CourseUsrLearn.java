package com.lyun.policelearning.entity;

import lombok.Data;

@Data
public class CourseUsrLearn {
    private int id;
    private int userId;
    private int courseId;
    private int teachId;
    private Long learnTime;

}
