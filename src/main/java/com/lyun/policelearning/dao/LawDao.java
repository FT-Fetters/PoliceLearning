package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.LawType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LawDao extends BaseDao<Law>{
}
