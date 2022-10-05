package com.lyun.policelearning.service.question.impl;

import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.QuestionImportService;
import com.lyun.policelearning.utils.IDocUtil;
import com.lyun.policelearning.utils.page.PageRequest;
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

    @Override
    public void importQuestion(String filePath) {
        List<Judgment> judgments = IDocUtil.getInstance().getJudgments(filePath);
        List<SingleChoice> singleChoices = IDocUtil.getInstance().getSingleChoice(filePath);
        List<MultipleChoice> multipleChoices = IDocUtil.getInstance().getMultipleChoice(filePath);
        for (Judgment judgment : judgments) {
            judgmentDao.newQuestion(judgment);
        }
        for (SingleChoice singleChoice : singleChoices) {
            singleChoiceDao.newQuestion(singleChoice);
        }
        for (MultipleChoice multipleChoice : multipleChoices) {
            multipleChoiceDao.newQuestion(multipleChoice);
        }
    }
}
