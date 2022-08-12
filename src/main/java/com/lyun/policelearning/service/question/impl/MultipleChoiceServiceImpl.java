package com.lyun.policelearning.service.question.impl;

import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MultipleChoiceServiceImpl implements MultipleChoiceService {

    @Autowired
    private MultipleChoiceDao multipleChoiceDao;

    @Autowired
    ErrorBookDao errorBookDao;

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
        errorBookDao.deleteById(3,id);
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

    @Override
    public int importQuestion(List<MultipleChoice> multipleChoices) {
        int num = 0;
        for (MultipleChoice multipleChoice : multipleChoices) {
            if (
                    multipleChoice.getAnswer() != null && !multipleChoice.getAnswer().equals("")
                            && multipleChoice.getOption_a() != null && !multipleChoice.getOption_a().equals("")
                            && multipleChoice.getOption_b() != null && !multipleChoice.getOption_b().equals("")
                            && multipleChoice.getOption_c() != null && !multipleChoice.getOption_c().equals("")
                            && multipleChoice.getOption_d() != null && !multipleChoice.getOption_d().equals("")
                            && multipleChoice.getProblem() != null && !multipleChoice.getProblem().equals("")
            ){
                try {
                    multipleChoiceDao.newQuestion(multipleChoice);
                    num++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return num;
    }

    @Override
    public PageResult selectByPage(PageRequest pageRequest) {
        return PageUtil.getPage(pageRequest,multipleChoiceDao.findAll());
    }
}
