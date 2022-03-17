package com.lyun.policelearning.utils;

import java.util.Base64;

public class JwtUtil {

    public static String getUserInfo(String jwt) {
        System.out.println(jwt.split("[.]")[1]);
        String tmp =jwt.split("[.]")[1].replaceAll("-", "+").replaceAll("_", "/");
        int i=tmp.length()%4;
        if(i==0) {

        }else if(i==2) {
            tmp=tmp+"==";
        }else if(i==3) {
            tmp=tmp+"=";
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(tmp.getBytes());

        return new String(bytes);
    }
}
