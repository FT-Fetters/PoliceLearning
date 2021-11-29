package com.lyun.policelearning.service.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SingleChoiceServiceImpl implements SingleChoiceService{

    @Autowired
    private SingleChoiceDao singleChoiceDao;

    @Override
    public List<SingleChoice> findAll() {
        return singleChoiceDao.findAll();
    }

    @Override
    public SingleChoice getById(int id) {
        return singleChoiceDao.getById(id);
    }

    @Override
    public void newQuestion(SingleChoice singleChoice) {
        singleChoiceDao.newQuestion(singleChoice);
    }

    @Override
    public void updateQuestion(SingleChoice singleChoice) {
        singleChoiceDao.updateQuestion(singleChoice);
    }
}
