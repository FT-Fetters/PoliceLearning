package com.lyun.policelearning.dao.course;

import com.lyun.policelearning.entity.course.CourseType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseTypeDao {
    List<CourseType> getAll();
    CourseType getById(int id);
    CourseType getByName(String name);
}
