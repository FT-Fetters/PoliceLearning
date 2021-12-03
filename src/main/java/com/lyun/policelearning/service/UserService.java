package com.lyun.policelearning.service;


public interface UserService {
    boolean check(String username,String password);
    int getPower(String username);
}
