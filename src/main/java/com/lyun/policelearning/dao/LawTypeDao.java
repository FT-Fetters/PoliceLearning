package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.LawType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LawTypeDao extends BaseDao<LawType>{
    List<LawType> findTitleByName(String name);
    void insertType(String lawtype);
    void updateTitleByName(String title,String lawtype);
    void deleteType(int id);
    LawType getTitleById(int id);
    void updateType(int id,String type);
}
