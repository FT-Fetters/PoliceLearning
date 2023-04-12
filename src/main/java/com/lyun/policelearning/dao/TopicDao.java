package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TopicDao {
    void publish(@Param("t")Topic topic);
    Topic getById(int id);
    void delete(int id);
    //List<Topic> all(String title);
    List<Topic> getAll(@Param("t") String title);
    List<Topic> myList(int uid);
}
