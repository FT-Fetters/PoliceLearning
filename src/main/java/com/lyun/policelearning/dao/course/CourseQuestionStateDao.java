package com.lyun.policelearning.dao.course;

import com.lyun.policelearning.entity.course.CourseQuestionState;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseQuestionStateDao {

    void submit(CourseQuestionState courseQuestionState);

    CourseQuestionState queryOne(int id);

    CourseQuestionState queryByContentAndUser(int contentId, int userId);

    void delete(int id);

    void deleteByContent(int contentId);

}
