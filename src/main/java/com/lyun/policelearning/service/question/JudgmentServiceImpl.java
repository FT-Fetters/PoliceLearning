package com.lyun.policelearning.service.question;

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
        judgmentDao.deleteQuestion(id);
    }
}
