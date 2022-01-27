package com.lyun.policelearning.service;


import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.User;
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
    public void newUser(String username,String password,String nickname,String realname,Integer role,String phone,String sex) {
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        userDao.newUser(username,password,nickname,realname,role,phone,sex);
    }

    @Override
    public void deleteUser(Integer id) {
        userDao.deleteUser(id);
    }

    @Override
    public void updateUser(Integer id, String username, String nickname, String realname, Integer role,String phone,String sex) {
        userDao.updateUser(id,username,nickname,realname,role,phone,sex);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void changePassword(String username, String password) {
        userDao.changePassword(username,DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
    }
}
