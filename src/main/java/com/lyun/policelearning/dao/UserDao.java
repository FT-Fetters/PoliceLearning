package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseDao<User>{
    User getByUsername(String username);
    int getPower(String username);
    User getById(int id);
    Integer count();
    void newUser(String username,String password,String nickname,String realname,int power);
    void deleteUser(int id);
    void updateUser(int id,String username,String password,String nickname,String realname,int power);
}
