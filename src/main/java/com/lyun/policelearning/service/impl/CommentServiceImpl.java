package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CommentDao;
import com.lyun.policelearning.dao.InformationDao;
import com.lyun.policelearning.dao.RuleDao;
import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.Comment;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    InformationDao informationDao;

    @Autowired
    RuleDao ruleDao;
    @Autowired
    UserDao userDao;

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
            if (comment.getParentId()!=null){
                continue;
            }
            JSONObject tmp = new JSONObject();
            comment.setNickName(userDao.getById(comment.getUserId()).getNickname());
            tmp.put("comment",comment);
            if (commentDao.findSecondComment(id,comment.getId(),"inf") != null){
               List<Comment> list = commentDao.findSecondComment(id,comment.getId(),"inf");
               for(Comment comment1 : list){
                   User user = userDao.getById(comment1.getUserId());
                   if (user == null) {
                       comment1.setNickName("用户已注销");
                   }else {
                       comment1.setNickName(user.getNickname());
                   }
               }
                tmp.put("secondComment",list);
            }else {
                tmp.put("secondComment",null);
            }
            res.add(tmp);
        }
        return res;
    }

    @Override
    public JSONArray getRuleComment(int id) {
        List<Comment> comments = commentDao.findComment(id,"rule");
        JSONArray res = new JSONArray();
        for (Comment comment : comments) {
            if (comment.getParentId()!=null){
                continue;
            }
            JSONObject tmp = new JSONObject();
            comment.setNickName(userDao.getById(comment.getUserId()).getNickname());
            tmp.put("comment",comment);
            if (commentDao.findSecondComment(id,comment.getId(),"rule") != null){
                List<Comment> list = commentDao.findSecondComment(id,comment.getId(),"rule");
                for(Comment comment1 : list){
                    comment1.setNickName(userDao.getById(comment1.getUserId()).getNickname());
                }
                tmp.put("secondComment",list);
            }else {
                tmp.put("secondComment",null);
            }
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
            if(comment1.getParentId() != null){
                //寻找parentId的昵称
                String parentNickName = commentDao.findParent(commentDao.findById(comment1.getParentId()).getUserId()).getNickname();
                jsonObject.put("parentNickName",parentNickName);
                jsonObject.put("toReplyContent",commentDao.findById(comment1.getParentId()).getContent());
                jsonObject.put("content",comment1.getContent());
                //寻找标题
                String title = null;
                if(comment1.getType().equals("inf")){
                    title = informationDao.getInformationById(comment1.getHostId()).getTitle();
                }else {
                    title = ruleDao.getRuleById(comment1.getHostId()).getTitle();
                }
                jsonObject.put("title",title);
                jsonObject.put("date",comment1.getDate());
                reply.add(jsonObject);
            }else {
               jsonObject.put("content",comment1.getContent());
               String title = null;
               if(comment1.getType().equals("inf")){
                   System.out.println("***********" + comment1);
                   if (informationDao.getInformationById(comment1.getHostId()) != null){
                       title = informationDao.getInformationById(comment1.getHostId()).getTitle();
                   }
               }else {
                   if (ruleDao.getRuleById(comment1.getHostId())!=null){
                       title = ruleDao.getRuleById(comment1.getHostId()).getTitle();
                   }
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
