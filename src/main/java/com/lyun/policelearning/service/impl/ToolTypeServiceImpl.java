package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.ToolTypeDao;
import com.lyun.policelearning.service.ToolService;
import com.lyun.policelearning.service.ToolTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolTypeServiceImpl implements ToolTypeService {
    @Autowired
    ToolTypeDao toolTypeDao;


    @Override
    public Object all() {
        return toolTypeDao.all();
    }

    @Override
    public void delete(int id) {
        toolTypeDao.delete(id);
    }

    @Override
    public void insert(String name) {
        toolTypeDao.insert(name);
    }
}
