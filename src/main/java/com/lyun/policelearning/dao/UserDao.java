package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseDao<User>{
    User getByUsername(String username);
    int getRole(String username);
    User getById(int id);
    Integer count();
    void newUser(String username,String password,String nickname,String realname,Integer role,String phone,String sex);
    void deleteUser(int id);
    void updateUser(int id,String password,String nickname,String realname,Integer role,String phone,String sex);
    void changePassword(String username,String password);
}
