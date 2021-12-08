package com.lyun.policelearning.dao;

import com.alibaba.fastjson.JSONArray;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.LawType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LawDao extends BaseDao<Law>{
    List<Law> findContent(String title);
    List<Law> findContentById(int id);
    void updateKeyword(int id,String str);
    Law findLawById(int id);
    void insert(String lawtype,String title,String content,String explaination,String crime,String keywords);
    void deleteById(int id);
    void updateById(int id, String lawtype,String title, String content, String explaination, String crime, String keyword);
}
