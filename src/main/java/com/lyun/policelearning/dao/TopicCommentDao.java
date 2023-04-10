package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.TopicComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TopicCommentDao {
    void publish(@Param("t")TopicComment topicComment);
    void delete(int id);
    List<TopicComment> getByTid(int tid);
}
