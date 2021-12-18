package com.lyun.policelearning.dao;


import com.lyun.policelearning.entity.Collect;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.Rule;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface CollectDao extends BaseDao<Collect> {
    void collect(int type, int articleId, int userId, Date date);
    List<Integer> findInformation(int userId);
    Information getInformation(int id);
    List<Integer> findLaw(int userId);
    Law getLaw(int id);
    List<Integer> findRule(int userId);
    Rule getRule(int id);
    void deleteCollect(int type,int articleId,int userId);
}
