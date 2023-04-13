package com.lyun.policelearning.dao.course;

import com.lyun.policelearning.dao.BaseDao;
import com.lyun.policelearning.entity.course.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseDao extends BaseDao<Course> {
    Course getCourseById(int id);
    Course getCourseByName(String name);
    void update(@Param("course") Course course);
    List<Course> getByType(String type);
    void publish(String name,String introduce,String type);
    Integer count();
    void delete(int id);
    List<Course> search(@Param("word") String word);
    void update1(@Param("course") Course course);
}
