package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.CourseContent;
import com.lyun.policelearning.utils.ResultBody;

import java.util.List;

public interface CourseContentService {

    ResultBody<?> getById(int id, int userId);

    ResultBody<?> getState(int id);

    ResultBody<?> save(CourseContent courseContent);

    ResultBody<?> update(CourseContent courseContent);

    ResultBody<?> delete(int id);

    List<CourseContent> getCourseContents(int courseId);
}
