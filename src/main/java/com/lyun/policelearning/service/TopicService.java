package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.Topic;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface TopicService {
    void publish(Topic topic);
    Topic getById(int id);
    void delete(int id);
    List<Topic> list(String title);
    Object getDetail(int id);
    PageResult findPage(PageRequest pageRequest,String title);
    Object getComment(int id);
}
