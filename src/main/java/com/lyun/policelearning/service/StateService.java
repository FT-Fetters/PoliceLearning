package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface StateService {
    boolean check(int tid,int uid);
    void insert(int tid,int uid);
    List<JSONObject> getState(int tid);
}
