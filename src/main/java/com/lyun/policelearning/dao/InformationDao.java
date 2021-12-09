package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InformationDao extends BaseDao<Information> {
    List<Information> findTop();
    List<Information> findNotTop();
    Information getInformationById(int id);
    void updateView(int id);
    //管理员部分
    List<Information> selectPage();
    void insertInformation(@Param("information") Information information,String path);
    void deleteById(int id);
    void updateById(@Param("information") Information information);
    void updateTopById(int id,int istop);
}
