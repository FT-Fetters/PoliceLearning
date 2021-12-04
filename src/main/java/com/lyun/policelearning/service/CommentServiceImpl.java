package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CommentDao;
import com.lyun.policelearning.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;


@Service
@Transactional
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentDao commentDao;

    @Override
    public List<Comment> findAll() {
        return commentDao.findAll();
    }

    @Override
    public void comment(int userId, Date date, String content, String type, int hostId) {
        commentDao.comment(userId, date, content, type, hostId);
    }

    @Override
    public void comment(int userId, int parentId, Date date, String content, String type, int hostId) {
        commentDao.secondComment(userId, parentId, date, content, type, hostId);
    }

    @Override
    public JSONArray getInfComment(int id) {
        List<Comment> comments = commentDao.findComment(id,"inf");
        JSONArray res = new JSONArray();
        for (Comment comment : comments) {
            JSONObject tmp = new JSONObject();
            tmp.put("comment",comment);
            List<Comment> secondComment = commentDao.findSecondComment(id,comment.getId(),"inf");
            tmp.put("secondComment",secondComment);
            res.add(tmp);
        }
        return res;
    }

    @Override
    public JSONArray getRuleComment(int id) {
        List<Comment> comments = commentDao.findComment(id,"rule");
        JSONArray res = new JSONArray();
        for (Comment comment : comments) {
            JSONObject tmp = new JSONObject();
            tmp.put("comment",comment);
            List<Comment> secondComment = commentDao.findSecondComment(id,comment.getId(),"rule");
            tmp.put("secondComment",secondComment);
            res.add(tmp);
        }
        return res;
    }
}
