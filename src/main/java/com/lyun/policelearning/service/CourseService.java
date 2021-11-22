package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Course;
import netscape.javascript.JSObject;

import java.util.List;

public interface CourseService {
    List<JSONObject> findAll();
}
