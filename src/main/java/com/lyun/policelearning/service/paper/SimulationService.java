package com.lyun.policelearning.service.paper;

import com.lyun.policelearning.entity.SimulationSettings;

public interface SimulationService {
    SimulationSettings getSettings();
    void updateSettings(SimulationSettings settings);

}
