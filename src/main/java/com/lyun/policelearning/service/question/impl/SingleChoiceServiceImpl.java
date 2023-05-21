package com.lyun.policelearning.service.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.SingleChoiceService;
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
public class SingleChoiceServiceImpl implements SingleChoiceService {

    @Autowired
    private SingleChoiceDao singleChoiceDao;

    @Autowired
    ErrorBookDao errorBookDao;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

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
        errorBookDao.deleteById(2, id);
        singleChoiceDao.deleteQuestion(id);
    }

    @Override
    public boolean check(int id, String answer) {
        SingleChoice singleChoice = singleChoiceDao.getById(id);
        return singleChoice.getAnswer().equalsIgnoreCase(answer);
    }

    @Override
    public int importQuestion(List<SingleChoice> singleChoices) {
        int num = 0;
        for (SingleChoice singleChoice : singleChoices) {
            if (
                    singleChoice.getAnswer() != null && !singleChoice.getAnswer().equals("")
                            && singleChoice.getOptionA() != null && !singleChoice.getOptionA().equals("")
                            && singleChoice.getOptionB() != null && !singleChoice.getOptionB().equals("")
                            && singleChoice.getOptionC() != null && !singleChoice.getOptionC().equals("")
                            && singleChoice.getOptionD() != null && !singleChoice.getOptionD().equals("")
                            && singleChoice.getProblem() != null && !singleChoice.getProblem().equals("")
            ) {
                try {
                    singleChoiceDao.newQuestion(singleChoice);
                    num++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return num;
    }

    @Override
    public PageResult selectByPage(PageRequest pageRequest) {
        return PageUtil.getPage(pageRequest, singleChoiceDao.findAll());
    }

    @Override
    public void batchDelete(JSONArray list) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        for (Object o : list) {
            Integer id = ((Integer) o);
            singleChoiceDao.deleteQuestion(id);
        }
        session.commit();
    }
}
