package com.lyun.policelearning.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.POJONode;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/manage")
public class UserManageApi {

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;


    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,userService.count());
    }

    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newUser(@RequestBody JSONObject data, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        String username = data.getString("username");
        String password = data.getString("password");
        String nickname = data.getString("nickname");
        String realname = data.getString("realname");
        Integer power = data.getInteger("power");
        if (username == null || password == null || power == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        if (userService.getByUsername(username) == null){
            userService.newUser(username,password,nickname,realname,power);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"username is exists");
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteUser(@RequestBody JSONObject data, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        Integer id = data.getInteger("id");
        if (id == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        userService.deleteUser(id);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateUser(@RequestBody JSONObject data,HttpServletRequest request){
        if (!UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        Integer id = data.getInteger("id");
        String username = data.getString("username");
        String password = data.getString("password");
        String nickname = data.getString("nickname");
        String realname = data.getString("realname");
        Integer power = data.getInteger("power");
        if (username == null || password == null || power == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        userService.updateUser(id,username,password,nickname,realname,power);
        return new ResultBody<>(true,200,null);
    }


}
