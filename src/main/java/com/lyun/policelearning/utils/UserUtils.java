package com.lyun.policelearning.utils;

import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {


    public static boolean checkPower(HttpServletRequest request,int power,UserService userService){
        String token = CookieUtils.getCookie(request,"token");
        if (token==null)return true;
        if (!User.Token.tokens.containsKey(token))return true;
        return userService.getPower(User.Token.tokens.get(token)) < power;
    }

    /**
     * 判断是否登录
     * @param request request
     * @return 如果登录则返回用户的id,如果未登录则返回-1
     */
    public static int isLogin(HttpServletRequest request,UserService userService){
        String token = CookieUtils.getCookie(request,"token");
        if (token==null)return -1;
        if (User.Token.tokens.containsKey(token)){
            User user = userService.getByUsername(User.Token.tokens.get(token));
            return user.getId();
        }else return -1;
    }

    public static String getUsername(HttpServletRequest request){
        String token = CookieUtils.getCookie(request,"token");
        if (token==null)return null;
        return User.Token.tokens.getOrDefault(token, null);
    }


}
