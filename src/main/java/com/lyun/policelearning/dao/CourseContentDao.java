package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.CourseContent;
import com.lyun.policelearning.utils.ResultBody;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseContentDao {


    CourseContent getById(long id);

    void save(CourseContent courseContent);

    void update(CourseContent courseContent);

    void delete(int id);

    List<CourseContent> getCourseContents(int courseId);

}
