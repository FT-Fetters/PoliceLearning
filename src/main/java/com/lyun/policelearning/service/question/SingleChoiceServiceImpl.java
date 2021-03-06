package com.lyun.policelearning.service.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.ErrorBookDao;
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
    @Autowired
    ErrorBookDao errorBookDao;

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

    @Override
    public void deleteQuestion(int id) {
        errorBookDao.deleteById(2,id);
        singleChoiceDao.deleteQuestion(id);
    }

    @Override
    public boolean check(int id, String answer) {
        SingleChoice singleChoice = singleChoiceDao.getById(id);
        return singleChoice.getAnswer().equalsIgnoreCase(answer);
    }
}
