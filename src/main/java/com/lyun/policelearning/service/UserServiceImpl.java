package com.lyun.policelearning.service;


import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

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
    public int getPower(String username) {
        return userDao.getPower(username);
    }
}
