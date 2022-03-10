package com.lyun.policelearning.service;


import com.lyun.policelearning.entity.User;

import java.util.List;

public interface UserService {
    boolean check(String username,String password);
    int getRole(String username);
    User getByUsername(String username);
    User getById(int id);
    int count();
    void newUser(String username,String password,String nickname,String realname,Integer role,String phone,String sex);
    void deleteUser(Integer id);
    void updateUser(Integer id,String username,String nickname,String realname,Integer role,String phone,String sex);
    List<User> findAll();
    void changePassword(String username,String password);
    void changeNickname(String username,String nickname);
}
