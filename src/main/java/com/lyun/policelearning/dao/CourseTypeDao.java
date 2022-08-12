package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.CourseType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseTypeDao {
    List<CourseType> getAll();
    CourseType getById(int id);
    CourseType getByName(String name);
}
