package com.lyun.policelearning.service;


import com.lyun.policelearning.controller.sys.user.UserApi;
import com.lyun.policelearning.entity.User;

import java.util.List;

public interface UserService {
    boolean check(String username,String password);
    int getRole(String username);
    User getByUsername(String username);
    User getById(int id);
    int count();
    void newUser(String username,String password,String nickname,String realname,Integer role,String phone,String sex,Long dept);
    void deleteUser(Integer id);
    void updateUser(Integer id,String username,String nickname,String realname,Integer role,String phone,String sex,Long dept);
    List<User> findAll();
    List<User> search(String word);
    void changePassword(String username,String password);
    void changeNickname(String username,String nickname);
    boolean isAdmin(int id);
    boolean isAdmin(User user);

    Object regInf(UserApi.RegInf body);
}
