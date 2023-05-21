package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao extends BaseDao<User>{
    User getByUsername(String username);
    int getRole(String username);
    User getById(int id);
    Integer count();
    void newUser(String username,String password,String nickname,String realname,Integer role,String phone,String sex,Long dept);
    void deleteUser(int id);
    void updateUser(int id,String username,String nickname,String realname,Integer role,String phone,String sex,String dept);
    void changePassword(String username,String password);
    void changeNickname(String username,String nickname);
    List<User> search(@Param("word") String word);
}
