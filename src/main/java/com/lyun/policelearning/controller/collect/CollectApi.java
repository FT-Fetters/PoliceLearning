package com.lyun.policelearning.controller.collect;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.CollectService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/collect")
@RestController
public class CollectApi {

    @Autowired
    CollectService collectService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object collect(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId != -1){
            String username = UserUtils.getUsername(request);
            Integer type = data.getInteger("type");
            Integer articleId = data.getInteger("articleId");
            if (type == null || articleId == null){
                LogUtils.log("collect but missing parameter","collect",true,request);
                return new ResultBody<>(false,501,"missing parameter");
            }
            if (type < 1 || type > 3){
                LogUtils.log(username+" collect but type is error","collect",false,request);
                return new ResultBody<>(false,502,"error type");
            }
            collectService.collect(type,articleId,userId);
            LogUtils.log(username+" collect article id " + articleId + ", type is " + type,"collect",false,request);
            return new ResultBody<>(true,200,null);
        }else {
            LogUtils.log("collect but not login","collect",true,request);
            return new ResultBody<>(false,500,"not login");
        }
    }
}
