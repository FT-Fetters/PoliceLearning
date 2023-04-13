package com.lyun.policelearning.entity.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseQuestion {
    private long id;
    private long contentId;
    private String question;
    private Boolean answer;
}

