package com.lyun.policelearning.service.question;

import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.entity.question.Judgment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JudgmentServiceImpl implements JudgmentService{

    @Autowired
    private JudgmentDao judgmentDao;

    @Autowired
    ErrorBookDao errorBookDao;

    @Override
    public List<Judgment> findAll() {
        return judgmentDao.findAll();
    }

    @Override
    public Judgment getById(int id) {
        return judgmentDao.getById(id);
    }

    @Override
    public void newQuestion(Judgment judgment) {
        judgmentDao.newQuestion(judgment);
    }

    @Override
    public void updateQuestion(Judgment judgment) {
        judgmentDao.updateQuestion(judgment);
    }

    @Override
    public void deleteQuestion(int id) {
        errorBookDao.deleteById(1,id);
        judgmentDao.deleteQuestion(id);
    }

    @Override
    public boolean check(int id, String answer) {
        Judgment judgment = judgmentDao.getById(id);
        return judgment.getAnswer().equals(answer);
    }
}
