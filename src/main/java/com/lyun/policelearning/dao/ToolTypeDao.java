package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.ToolType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ToolTypeDao {
    List<ToolType> all();
    void delete(int id);
    void insert(String name,int pid);
    void update(String name,int id);
    ToolType getById(int id);
    List<ToolType> getByPid(int pid);
}
