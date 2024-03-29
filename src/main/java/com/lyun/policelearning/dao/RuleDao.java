package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleDao extends BaseDao<Rule>{
    Rule getRuleById(int id);
    void updateView(int id);
    //管理员部分
    List<Rule> selectPage();
    List<Rule> search(@Param("word") String word);
    void insertRule(@Param("rule") Rule rule);
    void deleteById(int id);
    void updateById(@Param("rule") Rule rule);
    Integer count();
}
