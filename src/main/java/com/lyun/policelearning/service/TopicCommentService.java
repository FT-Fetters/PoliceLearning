package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.TopicComment;

public interface TopicCommentService {
    void publish(TopicComment topicComment);
    void delete(int id);
}
