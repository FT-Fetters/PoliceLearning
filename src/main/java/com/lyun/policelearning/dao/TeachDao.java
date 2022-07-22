package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Teach;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachDao {
    Teach getById(int id);
    Integer save(String content);
    void update(int id,String content);
}
