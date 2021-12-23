package com.lyun.policelearning.service.question;

import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MultipleChoiceServiceImpl implements MultipleChoiceService{

    @Autowired
    private MultipleChoiceDao multipleChoiceDao;

    @Override
    public List<MultipleChoice> findAll() {
        return multipleChoiceDao.findAll();
    }

    @Override
    public MultipleChoice getById(int id) {
        return multipleChoiceDao.getById(id);
    }

    @Override
    public void newQuestion(MultipleChoice multipleChoice) {
        multipleChoiceDao.newQuestion(multipleChoice);
    }

    @Override
    public void updateQuestion(MultipleChoice multipleChoice) {
        multipleChoiceDao.updateQuestion(multipleChoice);
    }

    @Override
    public void deleteQuestion(int id) {
        multipleChoiceDao.deleteQuestion(id);
    }

    @Override
    public boolean check(int id, String answer) {
        MultipleChoice multipleChoice = multipleChoiceDao.getById(id);
        return multipleChoice.getAnswer().equals(answer);
    }

    @Override
    public String getAnswer(int id) {
        return multipleChoiceDao.getAnswer(id);
    }
}
