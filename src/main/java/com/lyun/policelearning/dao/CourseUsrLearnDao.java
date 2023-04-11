package com.lyun.policelearning.dao;

import cn.hutool.json.JSONObject;
import com.lyun.policelearning.entity.CourseUsrLearn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseUsrLearnDao {

    List<CourseUsrLearn> queryByUserId(long userId, long courseId);

    List<CourseUsrLearn> queryByCourseAndContent(long courseId, long contentId);

    CourseUsrLearn queryOne(long userId, long courseId, long contentId);

    void update(CourseUsrLearn courseUsrLearn);

    void insert(CourseUsrLearn courseUsrLearn);

    void delete(int id);


}
