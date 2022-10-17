package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.State;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StateDao {
    State check(int tid, int uid);
    void insert(int tid,int uid);
    List<State> getState(int tid);
}
