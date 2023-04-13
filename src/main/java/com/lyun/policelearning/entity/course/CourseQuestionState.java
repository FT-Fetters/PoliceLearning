package com.lyun.policelearning.entity.course;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseQuestionState {
    private long id;
    private long userId;
    private long contentId;
    private String userInput;

}
