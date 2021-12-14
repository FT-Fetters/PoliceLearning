package com.lyun.policelearning.utils;

import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {


    public static boolean checkPower(HttpServletRequest request,int power,JwtConfig jwtConfig,UserService userService){
        String token = request.getHeader("token");
        String username = jwtConfig.getUsernameFromToken(token);
        if (token==null)return false;
        return userService.getPower(username) >= power;
    }

    public static String getUsername(HttpServletRequest request,JwtConfig jwtConfig){
        String token = request.getHeader("token");
        return jwtConfig.getUsernameFromToken(token);
    }

    public static int getUserId(HttpServletRequest request,JwtConfig jwtConfig){
        String token = request.getHeader("token");
        return jwtConfig.getUserIdFromToken(token);
    }
}
