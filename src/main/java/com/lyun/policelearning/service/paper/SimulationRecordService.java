package com.lyun.policelearning.service.paper;

import com.lyun.policelearning.entity.SimulationRecord;

import java.util.List;

public interface SimulationRecordService {
    void insert(SimulationRecord record);
    List<SimulationRecord> getByUserId(int userId);
    SimulationRecord getById(int id);

}
