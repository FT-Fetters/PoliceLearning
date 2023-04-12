package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.TopicComment;

public interface TopicCommentService {
    void publish(TopicComment topicComment);
    void delete(int id);
    void accept(int id,boolean isAccept);
}
