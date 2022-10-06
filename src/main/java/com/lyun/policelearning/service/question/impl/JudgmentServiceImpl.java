package com.lyun.policelearning.service.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class JudgmentServiceImpl implements JudgmentService {

    @Autowired
    private JudgmentDao judgmentDao;

    @Autowired
    ErrorBookDao errorBookDao;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

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

    @Override
    public int importQuestion(List<Judgment> judgments) {
        int num = 0;
        for (Judgment judgment : judgments) {
            if (
                    judgment.getAnswer() != null && !judgment.getAnswer().equals("")
                    && judgment.getOption_true() != null && !judgment.getOption_true().equals("")
                    && judgment.getOption_false() != null && !judgment.getOption_false().equals("")
                    && judgment.getProblem() != null && !judgment.getProblem().equals("")
            ){
                try {
                    judgmentDao.newQuestion(judgment);
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
        return PageUtil.getPage(pageRequest,judgmentDao.findAll());
    }

    @Override
    public void batchDelete(JSONArray list) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        for (Object o : list) {
            Integer id = ((Integer) o);
            judgmentDao.deleteQuestion(id);
        }
        session.commit();
    }
}
