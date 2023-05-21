package com.lyun.policelearning.service.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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

    @Autowired
    SqlSessionFactory sqlSessionFactory;

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
                            && multipleChoice.getOptionA() != null && !multipleChoice.getOptionA().equals("")
                            && multipleChoice.getOptionB() != null && !multipleChoice.getOptionB().equals("")
                            && multipleChoice.getOptionC() != null && !multipleChoice.getOptionC().equals("")
                            && multipleChoice.getOptionD() != null && !multipleChoice.getOptionD().equals("")
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

    @Override
    public void batchDelete(JSONArray list) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        for (Object o : list) {
            Integer id = ((Integer) o);
            multipleChoiceDao.deleteQuestion(id);
        }
        session.commit();
    }
}
