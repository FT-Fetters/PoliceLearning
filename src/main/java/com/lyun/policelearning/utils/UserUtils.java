package com.lyun.policelearning.utils;

import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {
    public static boolean checkPower(HttpServletRequest request,int power,UserService userService){
        String token = CookieUtils.getCookie(request,"token");
        if (token==null)return false;
        if (!User.Token.tokens.containsKey(token))return false;
        return userService.getPower(User.Token.tokens.get(token)) >= power;
    }
}
