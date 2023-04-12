package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.TopicCommentDao;
import com.lyun.policelearning.entity.TopicComment;
import com.lyun.policelearning.service.CommentService;
import com.lyun.policelearning.service.TopicCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicCommentServiceImpl implements TopicCommentService {
    @Autowired
    TopicCommentDao topicCommentDao;

    @Override
    public void publish(TopicComment topicComment) {
        topicCommentDao.publish(topicComment);
    }

    @Override
    public void delete(int id) {
        topicCommentDao.delete(id);
    }

    @Override
    public void accept(int id, boolean isAccept) {
        topicCommentDao.accept(id, isAccept);
    }
}
