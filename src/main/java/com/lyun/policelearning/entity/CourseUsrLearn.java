package com.lyun.policelearning.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseUsrLearn {
    private Long id;
    private Long userId;
    private Long courseId;
    private Long contentId;
    private Long learnTime;

}
