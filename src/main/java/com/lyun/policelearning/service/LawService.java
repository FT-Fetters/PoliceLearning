package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Law;

import java.util.List;

public interface LawService {
    List<JSONObject> findAll();
    JSONObject findAllType();
    JSONObject findTitleByName(String name);
    JSONObject findContent(String title);
    JSONObject findContentById(int id);
    boolean updateKeyword(String name,String explain,int id);
    boolean insert(String lawtype, String title, String content, String explaination, String crime, JSONArray keywords);
    boolean deleteById(int id);
    boolean updateById(int id,String lawtype,String title,String content,String explaination,String crime,JSONArray keyword);
    List<JSONObject> findTitleByNameForManage(String title);
    int count();
    void insertType(String lawtype);
    void deleteType(int id);
    void updateType(int id,String type);
}
