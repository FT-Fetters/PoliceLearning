package com.lyun.policelearning.utils;

import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.dao.RoleDao;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {



    public static boolean checkPower(HttpServletRequest request, int role, JwtConfig jwtConfig, UserService userService, RoleService roleService){
        String token = request.getHeader("token");
        String username = jwtConfig.getUsernameFromToken(token);
        if (token==null)return false;
        int roleId = userService.getRole(username);
        Integer needPower = roleService.findById(role).getPower();
        Integer userPower = roleService.findById(roleId).getPower();
        return userPower >= needPower;
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
