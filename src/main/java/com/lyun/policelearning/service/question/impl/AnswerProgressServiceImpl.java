package com.lyun.policelearning.service.question.impl;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.controller.paper.model.SequentialGetBody;
import com.lyun.policelearning.dao.question.AnswerProgressDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.AnswerProgress;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.AnswerProgressService;
import com.lyun.policelearning.utils.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AnswerProgressServiceImpl implements AnswerProgressService {

    @Autowired
    private AnswerProgressDao answerProgressDao;

    @Autowired
    private JudgmentDao judgmentDao;

    @Autowired
    private SingleChoiceDao singleChoiceDao;

    @Autowired
    private MultipleChoiceDao multipleChoiceDao;

    @Override
    public JSONObject getQuestion(SequentialGetBody body) {
        AnswerProgress answerProgress = answerProgressDao.getByUserId(body.getUserId());
        String progressId = answerProgress.getProgress_id();
        int jud = Integer.parseInt(progressId.split(",")[0]);
        int sin = Integer.parseInt(progressId.split(",")[1]);
        int mul = Integer.parseInt(progressId.split(",")[2]);
        List<Judgment> judgments = judgmentDao.selectNumsById(jud, 10);
        judgments.sort(Comparator.comparingInt(Judgment::getId));
        List<SingleChoice> singleChoices = singleChoiceDao.selectNumsById(sin, 10);
        singleChoices.sort(Comparator.comparingInt(SingleChoice::getId));
        List<MultipleChoice> multipleChoices = multipleChoiceDao.selectNumsById(mul, 10);
        multipleChoices.sort(Comparator.comparingInt(MultipleChoice::getId));
        JSONObject res = new JSONObject();
        JSONObject question = new JSONObject();
        question.put("j",new JSONObject());
        question.put("m",new JSONObject());
        question.put("s",new JSONObject());
        for (Judgment judgment : judgments) {
            question.getJSONArray("j").add(judgment);
        }
        for (SingleChoice singleChoice : singleChoices) {
            question.getJSONArray("s").add(singleChoice);
        }
        for (MultipleChoice multipleChoice : multipleChoices) {
            question.getJSONArray("m").add(multipleChoice);
        }
        res.put("question", question);
        progressId = judgments.get(9).getId() + ","
                + singleChoices.get(9).getId() + ","
                + multipleChoices.get(9).getId();
        AnswerProgress saveProcess = new AnswerProgress();
        saveProcess.setProgress_id(progressId);
        saveProcess.setUser_id(body.getUserId());
        saveProcess.setId(answerProgress.getId());
        answerProgressDao.update(saveProcess);
        return res;
    }
}
