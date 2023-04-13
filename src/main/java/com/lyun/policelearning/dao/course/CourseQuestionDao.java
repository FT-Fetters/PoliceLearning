package com.lyun.policelearning.dao.course;
import com.lyun.policelearning.entity.course.CourseQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CourseQuestionDao {
    List<CourseQuestion> queryByContentId(int contentId);

    void insert(CourseQuestion question);

    void delete(int id);

    void deleteByContent(int contentId);

    void update(CourseQuestion question);
}
