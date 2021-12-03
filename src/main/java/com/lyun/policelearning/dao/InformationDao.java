package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InformationDao extends BaseDao<Information> {
    Information getInformationById(int id);
    //管理员部分
    List<Information> selectPage();
    void insertInformation(@Param("information") Information information);
    void deleteById(int id);
    void updateById(@Param("information") Information information);
}
