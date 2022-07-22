package com.lyun.policelearning.service;

import com.lyun.policelearning.dao.TeachDao;
import com.lyun.policelearning.entity.Teach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachServiceImpl implements TeachService{
    @Autowired
    TeachDao teachDao;
    @Override
    public Teach getById(int id) {
        return teachDao.getById(id);
    }

    @Override
    public int save(String content) {
        return teachDao.save(content);
    }

    @Override
    public void update(int id, String content) {
        teachDao.update(id, content);
    }
}
