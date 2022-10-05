package com.lyun.policelearning.service.question.impl;

import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.QuestionImportService;
import com.lyun.policelearning.utils.IDocUtil;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.page.PageRequest;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


@Service
public class QuestionImportServiceImpl implements QuestionImportService {

    @Autowired
    private JudgmentDao judgmentDao;

    @Autowired
    private SingleChoiceDao singleChoiceDao;

    @Autowired
    private MultipleChoiceDao multipleChoiceDao;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void importQuestion(String filePath) {
        List<Judgment> judgments = IDocUtil.getInstance().getJudgments(filePath);
        List<SingleChoice> singleChoices = IDocUtil.getInstance().getSingleChoice(filePath);
        List<MultipleChoice> multipleChoices = IDocUtil.getInstance().getMultipleChoice(filePath);
        long curTime = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        for (Judgment judgment : judgments) {
            judgmentDao.newQuestion(judgment);
        }
        for (SingleChoice singleChoice : singleChoices) {
            singleChoiceDao.newQuestion(singleChoice);
        }
        for (MultipleChoice multipleChoice : multipleChoices) {
            multipleChoiceDao.newQuestion(multipleChoice);
        }
        session.commit();
        System.out.println("导入花费时间:" + (float)(System.currentTimeMillis() - curTime)/1000.0);
    }
}
