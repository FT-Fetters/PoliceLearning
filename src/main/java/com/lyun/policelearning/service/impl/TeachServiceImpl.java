package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.TeachDao;
import com.lyun.policelearning.entity.Teach;
import com.lyun.policelearning.service.TeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachServiceImpl implements TeachService {
    @Autowired
    TeachDao teachDao;
    @Override
    public Teach getById(int id) {
        return teachDao.getById(id);
    }

    @Override
    public int save(Teach teach) {
        teachDao.save(teach);
        System.out.println(teach.getId());
        return teach.getId();
    }

    @Override
    public void update(int id, String content) {
        teachDao.update(id, content);
    }

    @Override
    public void delete(int id) {
        teachDao.delete(id);
    }
}
