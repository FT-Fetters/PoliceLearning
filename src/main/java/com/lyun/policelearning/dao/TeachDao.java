package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Teach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeachDao {
    Teach getById(int id);
    void save(Teach teach);
    void update(int id,String content);
}
