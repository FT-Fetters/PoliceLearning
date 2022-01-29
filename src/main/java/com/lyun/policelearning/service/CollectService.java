package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Collect;

import java.sql.Date;
import java.util.List;

public interface CollectService {
    List<Collect> findAll();
    boolean collect(int type, int articleId,int userId);
    Object findCollect(int type,int userId);
    void deleteCollect(int type,int articleId,int userId);
}
