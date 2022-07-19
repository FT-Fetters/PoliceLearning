package com.lyun.policelearning.dao.paper;

import com.lyun.policelearning.entity.Paper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaperDao {
    List<Paper> selectAll();
    int insert(Paper paper);
    Paper getById(int id);
    void delete(int id);
    void setState(int id,int enable);
}
