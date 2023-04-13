package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.course.CourseQuestion;
import com.lyun.policelearning.utils.ResultBody;

import java.util.List;

public interface CourseQuestionService {

    ResultBody<?> getCourseQuestion(int contentId);

    ResultBody<?> insert(CourseQuestion courseQuestion);

    ResultBody<?> delete(int id);

    ResultBody<?> update(CourseQuestion courseQuestion);

    ResultBody<?> submit(List<Boolean> inputs, int contentId, int userId);

    ResultBody<?> checkFinished(int contentId, int userId);
}
