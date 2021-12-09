package com.lyun.policelearning.dao;


import com.lyun.policelearning.entity.Collect;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface CollectDao extends BaseDao<Collect> {
    void collect(int type, int articleId, int userId, Date date);
}
