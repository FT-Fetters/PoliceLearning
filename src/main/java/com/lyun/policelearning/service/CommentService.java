package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Comment;

import java.sql.Date;
import java.util.List;

public interface CommentService {
    List<Comment> findAll();
    void comment(int userId, Date date, String content, String type, int hostId);
    void comment(int userId,int parentId,Date date,String content,String type,int hostId);
    JSONArray getInfComment(int id);
    JSONArray getRuleComment(int id);
}
