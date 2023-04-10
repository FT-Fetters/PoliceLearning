package com.lyun.policelearning.dao;

import cn.hutool.json.JSONObject;
import com.lyun.policelearning.entity.CourseUsrLearn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseUsrLearnDao {

    List<CourseUsrLearn> queryByUserId(int userId);
}
