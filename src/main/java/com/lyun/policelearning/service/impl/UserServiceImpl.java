package com.lyun.policelearning.service.impl;


import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public boolean check(String username, String password) {
        User user = userDao.getByUsername(username);
        if (user == null){
            return false;
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        return user.getPassword().equals(password);
    }

    @Override
    public int getRole(String username) {
        return userDao.getRole(username);
    }

    @Override
    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    public User getById(int id) {
        return userDao.getById(id);
    }

    @Override
    public int count() {
        return userDao.count();
    }

    @Override
    public void newUser(String username,String password,String nickname,String realname,Integer role,String phone,String sex,Long dept) {
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        userDao.newUser(username,password,nickname,realname,role,phone,sex,dept);
    }

    @Override
    public void deleteUser(Integer id) {
        userDao.deleteUser(id);
    }

    @Override
    public void updateUser(Integer id, String username, String nickname, String realname, Integer role,String phone,String sex,Long dept) {
        userDao.updateUser(id,username,nickname,realname,role,phone,sex,dept);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> search(String word) {
        return userDao.search(word);
    }

    @Override
    public void changePassword(String username, String password) {
        userDao.changePassword(username,DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void changeNickname(String username, String nickname) {
        userDao.changeNickname(username,nickname);
    }
}
