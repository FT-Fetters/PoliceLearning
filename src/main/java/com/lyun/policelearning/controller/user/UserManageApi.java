package com.lyun.policelearning.controller.user;

import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/manage")
public class UserManageApi {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,userService.count());
    }
}
