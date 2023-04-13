package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Tool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ToolDao {
    List<Tool> all(@Param("type") int type);
    List<Tool> list(int type,String title);
    List<Tool> findByTitle(@Param("title") String title);
    Tool getById(int id);
    void insert(@Param("t")Tool tool);
    void update(@Param("t")Tool tool);
    void delete(int id);
}
