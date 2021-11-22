package com.lyun.policelearning.service;

import com.lyun.policelearning.dao.InformationDao;
import com.lyun.policelearning.entity.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InformationServiceImpl implements InformationService{

    @Autowired
    private InformationDao informationDao;

    @Override
    public List<Information> findAll() {
        return informationDao.findAll();
    }
}
