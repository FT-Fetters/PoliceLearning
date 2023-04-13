package com.lyun.policelearning.dao.course;

import com.lyun.policelearning.entity.course.CourseContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseContentDao {


    CourseContent getById(long id);

    void save(CourseContent courseContent);

    void update(CourseContent courseContent);

    void delete(int id);

    void deleteByCourseId(int courseId);

    List<CourseContent> getCourseContents(long courseId);


}
