package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CommentDao;
import com.lyun.policelearning.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Override
    public HashMap<String, List<JSONObject>> getCommentAndReply(int userId) {
        HashMap<String,List<JSONObject>> res = new HashMap<>();
        List<JSONObject> comment = new ArrayList<>();
        List<JSONObject> reply = new ArrayList<>();
        for(Comment comment1 : commentDao.findCommentAndReply(userId)){
            JSONObject jsonObject = new JSONObject();
            if(comment1.getParentId() > 0){
                //寻找parentId的昵称
                String parentNickName = commentDao.findParent(comment1.getParentId()).getNickname();
                jsonObject.put("parentNickName",parentNickName);
                jsonObject.put("content",comment1.getContent());
                //寻找标题
                String title = null;
                if(comment1.getType().equals("inf")){
                    title = commentDao.findInfTitle(comment1.getHostId()).getTitle();
                }else {
                    title = commentDao.findRuleTitle(comment1.getHostId()).getTitle();
                }
                jsonObject.put("title",title);
                jsonObject.put("date",comment1.getDate());
                reply.add(jsonObject);
            }else {
               jsonObject.put("content",comment1.getContent());
               String title = null;
               if(comment1.getType().equals("inf")){
                   title = commentDao.findInfTitle(comment1.getHostId()).getTitle();
               }else {
                   title = commentDao.findRuleTitle(comment1.getHostId()).getTitle();
               }
               jsonObject.put("title",title);
               jsonObject.put("date",comment1.getDate());
               comment.add(jsonObject);
            }
        }
        res.put("comment",comment);
        res.put("reply",reply);
        return res;
    }

    @Override
    public void deleteComment(int id) {
        commentDao.deleteComment(id);
    }
}
