package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ErrorBookService {
    void save(int userId,int type,int questionId);
    List<JSONObject> findAll(int userId);
    void delete(int userId,int type,int questionId);
}
