package com.lyun.policelearning.controller;


import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/list")
    public Object list(HttpServletRequest request){
        if (UserUtils.checkPower(request, 5, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        LogUtils.log("get logs list","get",true,request);
        String path = PathTools.getRunPath() + "/log/";
        File logDir = new File(path);
        File[] logs = logDir.listFiles();
        List<String> logList = new ArrayList<>();
        assert logs != null;
        for (File log : logs) {
            logList.add(log.getAbsolutePath());
        }
        return new ResultBody<>(true,200,logList);

    }

}
