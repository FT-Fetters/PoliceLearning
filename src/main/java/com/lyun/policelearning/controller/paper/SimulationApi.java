package com.lyun.policelearning.controller.paper;

import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.SimulationRecord;
import com.lyun.policelearning.entity.SimulationSettings;
import com.lyun.policelearning.service.paper.SimulationRecordService;
import com.lyun.policelearning.service.paper.SimulationService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/simulation")
@Permission
public class SimulationApi {


    @Autowired
    private SimulationService simulationService;

    @Autowired
    private SimulationRecordService simulationRecordService;

    @Autowired
    private JwtConfig jwtConfig;


    @GetMapping("/get/settings")
    @Permission(admin = false)
    public Object getSettings(){
        return new ResultBody<>(true,200,simulationService.getSettings());
    }

    @PostMapping("/update/settings")
    public Object updateSettings(@RequestBody SimulationSettings settings){
        simulationService.updateSettings(settings);
        return new ResultBody<>(true,200,null);
    }

    @PostMapping("/put/record")
    @Permission(admin = false)
    public Object insertRecord(@RequestBody SimulationRecord record,HttpServletRequest request){
        int userId = UserUtils.getUserId(request, jwtConfig);
        record.setUser_id(userId);
        simulationRecordService.insert(record);
        return new ResultBody<>(true,200,null);
    }

    @GetMapping("/get/user/record")
    @Permission(admin = false)
    public Object getAllRecord(HttpServletRequest request){
        int userId = UserUtils.getUserId(request, jwtConfig);
        return new ResultBody<>(true,200,simulationRecordService.getByUserId(userId));
    }



}
