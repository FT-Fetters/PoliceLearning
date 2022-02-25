package com.lyun.policelearning.entity;


import lombok.Data;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Data
public class User {
//    public static class Token {
//        /**
//         * tokens列表
//         * key: token
//         * value: username
//         */
//        public static HashMap<String,String> tokens = new HashMap<>();
//
//        /**
//         *
//         */
//        public static HashMap<String,Long> tokenTime = new HashMap<>();
//
//        /**
//         * 创建token并返回
//         * @param username 用户名
//         * @return token
//         */
//        public static String createToken(String username){
//            String tmp = username + System.currentTimeMillis() + "policelearing";
//            String token = DigestUtils.md5DigestAsHex(tmp.getBytes(StandardCharsets.UTF_8));
//            tokens.put(token,username);
//            tokenTime.put(token,System.currentTimeMillis());
//            return token;
//        }
//
//        /**
//         * 检擦是否存在token并对比username
//         * @param username 用户名
//         * @param token token
//         * @return 是否存在token与username对应
//         */
//        public static boolean checkToken(String username,String token){
//            if (tokens.containsKey(token)){
//                if (System.currentTimeMillis() - tokenTime.get(token) > 3600*1000){
//                    dropToken(username,token);
//                    return false;
//                }
//                return tokens.get(token).equals(username);
//            }else return false;
//        }
//
//        /**
//         * 删除token
//         * @param username 用户名
//         * @param token token
//         */
//        public static void dropToken(String username,String token){
//            if (tokens.containsKey(token)){
//                if (tokens.get(token).equals(username)){
//                    tokens.remove(token);
//                    tokenTime.remove(token);
//                }
//            }
//        }
//
//    }
    private int id;
    private String username;
    private String nickname;
    private String password;
    private int role;
    private String realname;
    private String phone;
    private String sex;
}
