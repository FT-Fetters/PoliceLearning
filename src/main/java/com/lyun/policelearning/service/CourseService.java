package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.util.List;

public interface CourseService {
    List<JSONObject> findAll();
    JSONObject getCourseById(int id);
    JSONArray getCatalogue(int id);
    String getIntroduce(int id);
    boolean changeType(int id,String type);
    JSONArray getByType(String type);
    JSONObject getCourseByName(String name);
    boolean changeIntroduce(int id,String introduce);
    boolean publish(String name,String introduce,String type);
    boolean changeCatalogue(int id,JSONArray catalogue);
    boolean changeCatalogue(String name,JSONArray catalogue);
    Integer count();
    void delete(int id);
    List<JSONObject> search(String word);
}
