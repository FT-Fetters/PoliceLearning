package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.dao.TopicCommentDao;
import com.lyun.policelearning.dao.TopicDao;
import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.Topic;
import com.lyun.policelearning.entity.TopicComment;
import com.lyun.policelearning.service.TopicService;
import com.lyun.policelearning.utils.Constant;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    TopicDao topicDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TopicCommentDao topicCommentDao;
    public static Page page;


    @Override
    public void publish(Topic topic) {
        topicDao.publish(topic);
    }

    @Override
    public Topic getById(int id) {
        return topicDao.getById(id);
    }

    @Override
    public void delete(int id) {
        topicDao.delete(id);
    }

    @Override
    public List<Topic> list(String title) {
        List<Topic> res = topicDao.getAll(title);
        for (Topic topic : res){
            topic.setRealName(userDao.getById(topic.getUid()).getRealname());
            topic.setContent(null);
            if (topic.getPicture() != null && !"".equals(topic.getPicture())){
                topic.setPicture(Constant.BASE_URL + "api/upload/topicPicture/" + topic.getPicture());
            }
        }
        return res;
    }

    @Override
    public Object getDetail(int id) {
        Topic topic = topicDao.getById(id);
        JSONObject res = new JSONObject();
        res.put("id",topic.getId());
        res.put("title",topic.getTitle());
        res.put("content",topic.getContent());
        res.put("picture",Constant.BASE_URL + "api/upload/topicPicture/" + topic.getPicture());
        res.put("date",topic.getDate());
        //获取评论
        List<TopicComment> topicCommentList = topicCommentDao.getByTid(topic.getId());
        for(TopicComment topicComment : topicCommentList){
            topicComment.setRealName(userDao.getById(topicComment.getUid()).getRealname());
        }
        res.put("comment",topicCommentList);
        return res;
    }

    @Override
    public PageResult findPage(PageRequest pageRequest, String title) {
        return PageUtil.getPageResult(getPageInfo(pageRequest,title),page);
    }

    @Override
    public Object getComment(int id) {
        List<TopicComment> topicCommentList = topicCommentDao.getByTid(id);
        for(TopicComment topicComment : topicCommentList){
            topicComment.setRealName(userDao.getById(topicComment.getUid()).getRealname());
        }
        return topicCommentList;
    }

    @Override
    public Object getMyList(int uid) {
        List<Topic> res = topicDao.myList(uid);
        for (Topic topic : res){
            topic.setRealName(userDao.getById(topic.getUid()).getRealname());
            topic.setContent(null);
            if (topic.getPicture() != null && !"".equals(topic.getPicture())){
                topic.setPicture(Constant.BASE_URL + "api/upload/topicPicture/" + topic.getPicture());
            }
        }
        return res;
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest,String title) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Topic topic : topicDao.getAll(title)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",topic.getId());
            jsonObject.put("realName",userDao.getById(topic.getUid()).getRealname());
            jsonObject.put("title",topic.getTitle());
            if (topic.getPicture() != null && !"".equals(topic.getPicture())){
                jsonObject.put("picture",Constant.BASE_URL + "api/upload/topicPicture/" + topic.getPicture());
            }else {
                jsonObject.put("picture",null);
            }
            jsonObject.put("date",topic.getDate());
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }
}
