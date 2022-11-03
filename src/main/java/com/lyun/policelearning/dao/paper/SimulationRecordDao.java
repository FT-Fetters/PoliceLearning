package com.lyun.policelearning.dao.paper;

import com.lyun.policelearning.entity.SimulationRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SimulationRecordDao {
    void insert(SimulationRecord record);
    List<SimulationRecord> getByUserId(int userId);
    SimulationRecord getById(int id);
}
