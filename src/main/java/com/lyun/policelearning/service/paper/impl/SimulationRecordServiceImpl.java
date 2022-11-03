package com.lyun.policelearning.service.paper.impl;

import com.lyun.policelearning.dao.paper.SimulationRecordDao;
import com.lyun.policelearning.entity.SimulationRecord;
import com.lyun.policelearning.service.paper.SimulationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class SimulationRecordServiceImpl implements SimulationRecordService {

    @Autowired
    private SimulationRecordDao simulationRecordDao;

    @Override
    public void insert(SimulationRecord record) {
        record.setDate(new Date(System.currentTimeMillis()));
        record.setTime(new Time(System.currentTimeMillis()));
        simulationRecordDao.insert(record);
    }

    @Override
    public List<SimulationRecord> getByUserId(int userId) {
        return simulationRecordDao.getByUserId(userId);
    }

    @Override
    public SimulationRecord getById(int id) {
        return simulationRecordDao.getById(id);
    }
}
