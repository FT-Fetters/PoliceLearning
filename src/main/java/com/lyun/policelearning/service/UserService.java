package com.lyun.policelearning.service;


import com.lyun.policelearning.entity.User;

public interface UserService {
    boolean check(String username,String password);
    int getPower(String username);
    User getByUsername(String username);
    User getById(int id);
    int count();
}
