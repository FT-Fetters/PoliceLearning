package com.lyun.policelearning.service;


import com.lyun.policelearning.entity.User;

import java.util.List;

public interface UserService {
    boolean check(String username,String password);
    int getPower(String username);
    User getByUsername(String username);
    User getById(int id);
    int count();
    void newUser(String username,String password,String nickname,String realname,Integer power);
    void deleteUser(Integer id);
    void updateUser(Integer id,String username,String nickname,String realname,int power);
    List<User> findAll();
    void changePassword(String username,String password);
}
