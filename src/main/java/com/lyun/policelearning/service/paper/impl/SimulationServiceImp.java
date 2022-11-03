package com.lyun.policelearning.service.paper.impl;

import com.lyun.policelearning.dao.paper.SimulationDao;
import com.lyun.policelearning.entity.SimulationSettings;
import com.lyun.policelearning.service.paper.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationServiceImp implements SimulationService {

    @Autowired
    private SimulationDao simulationDao;

    @Override
    public SimulationSettings getSettings() {
        return simulationDao.getSettings().get(0);
    }

    @Override
    public void updateSettings(SimulationSettings settings) {
        simulationDao.updateSettings(settings);
    }
}
