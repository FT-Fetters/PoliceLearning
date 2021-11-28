package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface LawService {
    List<JSONObject> findAll();
    JSONObject findAllType();
    JSONObject findTitleByName(String name);
    JSONObject findContent(String title);
    JSONObject findContentById(int id);
    boolean updateKeyword(String name,String explain,int id);
}
