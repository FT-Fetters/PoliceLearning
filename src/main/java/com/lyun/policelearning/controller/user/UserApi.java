package com.lyun.policelearning.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.CookieUtils;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    UserService userService;

    public Object login(@RequestBody JSONObject data, HttpServletResponse response){
        String username = data.getString("username");
        String password = data.getString("password");
        if (username != null && password != null && userService.check(username,password)){
            String token = User.Token.createToken(username);
            CookieUtils.writeCookie(response,"token",token,3600);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"error password or username is not exist");
        }
    }
}
