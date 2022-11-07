package com.lyun.policelearning.service.question.impl;

import com.alibaba.fastjson.JSONArray;
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
        if (answerProgress == null){
            AnswerProgress tmp = new AnswerProgress();
            tmp.setUser_id(body.getUserId());
            tmp.setProgress("0,0,0");
            answerProgressDao.insert(tmp);
            answerProgress = answerProgressDao.getByUserId(body.getUserId());
        }
        String progressId = answerProgress.getProgress();
        int jud = Integer.parseInt(progressId.split(",")[0]);
        int sin = Integer.parseInt(progressId.split(",")[1]);
        int mul = Integer.parseInt(progressId.split(",")[2]);
        List<Judgment> judgments = judgmentDao.selectNumsById(jud, body.getJud());
        judgments.sort(Comparator.comparingInt(Judgment::getId));
        List<SingleChoice> singleChoices = singleChoiceDao.selectNumsById(sin, body.getSin());
        singleChoices.sort(Comparator.comparingInt(SingleChoice::getId));
        List<MultipleChoice> multipleChoices = multipleChoiceDao.selectNumsById(mul, body.getMul());
        multipleChoices.sort(Comparator.comparingInt(MultipleChoice::getId));
        JSONObject res = new JSONObject();
        JSONObject question = new JSONObject();
        question.put("j",new JSONArray());
        question.put("m",new JSONArray());
        question.put("s",new JSONArray());
        JSONObject tmp;
        for (Judgment judgment : judgments) {
            tmp = new JSONObject();
            tmp.put("id",judgment.getId());
            tmp.put("problem",judgment.getProblem());
            tmp.put("type",1);
            JSONArray option = new JSONArray();
            JSONObject option_true = new JSONObject();
            option_true.put("id","A");
            option_true.put("content",judgment.getOption_true());
            option.add(option_true);
            JSONObject option_false = new JSONObject();
            option_false.put("id","B");
            option_false.put("content",judgment.getOption_false());
            option.add(option_false);
            tmp.put("option",option);
            tmp.put("answer",judgment.getAnswer());
            question.getJSONArray("j").add(tmp);
        }
        for (SingleChoice singleChoice : singleChoices) {
            tmp = new JSONObject();
            tmp.put("id",singleChoice.getId());
            tmp.put("problem",singleChoice.getProblem());
            tmp.put("type",2);
            JSONArray option = new JSONArray();
            JSONObject option_a = new JSONObject();
            option_a.put("id","A");
            option_a.put("content",singleChoice.getOption_a());
            option.add(option_a);
            JSONObject option_b = new JSONObject();
            option_b.put("id","B");
            option_b.put("content",singleChoice.getOption_b());
            option.add(option_b);
            JSONObject option_c = new JSONObject();
            option_c.put("id","C");
            option_c.put("content",singleChoice.getOption_c());
            option.add(option_c);
            JSONObject option_d = new JSONObject();
            option_d.put("id","D");
            option_d.put("content",singleChoice.getOption_d());
            option.add(option_d);
            tmp.put("option",option);
            tmp.put("answer",singleChoice.getAnswer());
            question.getJSONArray("s").add(tmp);
        }
        for (MultipleChoice multipleChoice : multipleChoices) {
            tmp = new JSONObject();
            tmp.put("id",multipleChoice.getId());
            tmp.put("problem",multipleChoice.getProblem());
            tmp.put("type",3);
            JSONArray option = new JSONArray();
            JSONObject option_a = new JSONObject();
            option_a.put("id","A");
            option_a.put("content",multipleChoice.getOption_a());
            option.add(option_a);
            JSONObject option_b = new JSONObject();
            option_b.put("id","B");
            option_b.put("content",multipleChoice.getOption_b());
            option.add(option_b);
            JSONObject option_c = new JSONObject();
            option_c.put("id","C");
            option_c.put("content",multipleChoice.getOption_c());
            option.add(option_c);
            JSONObject option_d = new JSONObject();
            option_d.put("id","D");
            option_d.put("content",multipleChoice.getOption_d());
            option.add(option_d);
            tmp.put("option",option);
            tmp.put("answer",multipleChoice.getAnswer());
            question.getJSONArray("m").add(tmp);
        }
        res.put("question", question);
        progressId = (body.getJud() > 0 && judgments.size() > 0 ? judgments.get(judgments.size() - 1).getId() : jud) + ","
                + (body.getSin() > 0 && singleChoices.size() > 0 ? singleChoices.get(singleChoices.size() - 1).getId() : sin) + ","
                + (body.getMul() > 0 && multipleChoices.size() > 0 ? multipleChoices.get(multipleChoices.size() - 1).getId() : mul);

        AnswerProgress saveProcess = new AnswerProgress();
        saveProcess.setProgress(progressId);
        saveProcess.setUser_id(body.getUserId());
        saveProcess.setId(answerProgress.getId());
        answerProgressDao.update(saveProcess);
        return res;
    }

    @Override
    public JSONObject getProgress(int userId) {
        AnswerProgress answerProgress = answerProgressDao.getByUserId(userId);
        if (answerProgress == null){
            AnswerProgress tmp = new AnswerProgress();
            tmp.setUser_id(userId);
            tmp.setProgress("0,0,0");
            answerProgressDao.insert(tmp);
            answerProgress = tmp;
        }
        String progress = answerProgress.getProgress();
        JSONObject res = new JSONObject();
        JSONObject jud = new JSONObject();
        jud.put("finished", judgmentDao.countLessThanId(Integer.parseInt(progress.split(",")[0])));
        jud.put("all", judgmentDao.count());
        res.put("j",jud);
        JSONObject sin = new JSONObject();
        sin.put("finished", singleChoiceDao.countLessThanId(Integer.parseInt(progress.split(",")[1])));
        sin.put("all", singleChoiceDao.count());
        res.put("s",sin);
        JSONObject mul = new JSONObject();
        mul.put("finished", multipleChoiceDao.countLessThanId(Integer.parseInt(progress.split(",")[2])));
        mul.put("all", multipleChoiceDao.count());
        res.put("m",mul);
        return res;
    }
}
