package com.lyun.policelearning.service;


import com.lyun.policelearning.dao.CollectDao;
import com.lyun.policelearning.entity.Collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class CollectServiceImpl implements CollectService{

    @Autowired
    private CollectDao collectDao;

    @Override
    public List<Collect> findAll() {
        return collectDao.findAll();
    }

    @Override
    public void collect(int type, int articleId,int userId) {
        Date date = new Date(System.currentTimeMillis());
        collectDao.collect(type,articleId,userId,date);
    }
}
