package com.lyun.policelearning.controller;


import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/log")
@RestController
public class LogApi {

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;


    @RequestMapping("/list")
    public Object list(HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1,jwtConfig, userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        LogUtils.log("get logs list","get",true,request);
        String path = PathTools.getRunPath() + "/log/";
        File logDir = new File(path);
        File[] logs = logDir.listFiles();
        List<String> logList = new ArrayList<>();
        assert logs != null;
        for (File log : logs) {
            logList.add(log.getName());
        }
        return new ResultBody<>(true,200,logList);

    }

    @SneakyThrows
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object get(@RequestParam String log, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1,jwtConfig, userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        String path = PathTools.getRunPath() + "/log/";
        File logFile = new File(path + log);
        if (!logFile.exists())return new ResultBody<>(false,500,"log is not exists");
        String logStr = FileUtils.txt2String(logFile);
        return new ResultBody<>(true,200,logStr);

    }

}
