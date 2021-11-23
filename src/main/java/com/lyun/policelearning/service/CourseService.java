package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Course;
import netscape.javascript.JSObject;

import java.util.List;

public interface CourseService {
    List<JSONObject> findAll();
    JSONObject getCourseById(int id);
    JSONArray getCatalogue(int id);
    String getIntroduce(int id);
    boolean changeType(int id,String type);
    JSONArray getByType(String type);
    boolean changeIntroduce(int id,String introduce);
}
