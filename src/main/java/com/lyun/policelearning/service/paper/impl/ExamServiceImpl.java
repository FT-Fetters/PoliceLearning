package com.lyun.policelearning.service.paper.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.dao.paper.ExamDao;
import com.lyun.policelearning.dao.paper.PaperDao;
import com.lyun.policelearning.dao.paper.PaperQuestionDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.Exam;
import com.lyun.policelearning.entity.PaperQuestion;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.paper.ExamService;
import com.lyun.policelearning.service.paper.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperQuestionDao paperQuestionDao;

    @Autowired
    private ExamDao examDao;

    @Autowired
    private JudgmentDao judgmentDao;

    @Autowired
    private MultipleChoiceDao multipleChoiceDao;

    @Autowired
    private SingleChoiceDao singleChoiceDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Override
    public float submit(int userId, int paperId, List<String> inputs) {
        if (examDao.selectByUserIdAndPaperId(userId,paperId)!=null)return -1;
        List<PaperQuestion> paperQuestions = paperQuestionDao.selectByPaperId(paperId);
        paperQuestions.sort(Comparator.comparingInt(PaperQuestion::getIndex));
        float scorePerQue = (float) (100.0/((float)paperQuestions.size()));
        float sumScore = 0;
        for (int i = 0; i < paperQuestions.size(); i++) {
            switch (paperQuestions.get(i).getType()){
                case 'j':
                    Judgment judgment = judgmentDao.getById(paperQuestions.get(i).getQuestion_id());
                    if (judgment.getAnswer().equals(inputs.get(i)))sumScore+=scorePerQue;
                    break;
                case 'm':
                    MultipleChoice multipleChoice = multipleChoiceDao.getById(paperQuestions.get(i).getQuestion_id());
                    if (multipleChoice.getAnswer().equals(inputs.get(i)))sumScore+=scorePerQue;
                    break;
                case 's':
                    SingleChoice singleChoice = singleChoiceDao.getById(paperQuestions.get(i).getQuestion_id());
                    if (singleChoice.getAnswer().equals(inputs.get(i)))sumScore+=scorePerQue;
                    break;
            }
        }
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < inputs.size(); i++) {
            input.append(inputs.get(i)).append(i==inputs.size()-1?"":",");
        }
        examDao.submit(userId,paperId,new Date(System.currentTimeMillis()),sumScore,input.toString());
        return sumScore;
    }

    @Override
    public List<Exam> selectByUserId(int user_id) {
        return examDao.selectByUserId(user_id);
    }

    @Override
    public JSONObject getExamScore(int paper_id, int user_id) {
        JSONObject res = new JSONObject();
        Exam exam = examDao.selectByUserIdAndPaperId(user_id, paper_id);
        if (exam!=null) {
            res.put("score",exam.getScore());
            JSONObject paper = paperService.userGetById(paper_id);
            res.put("paper",paper);
            res.put("user_input",exam.getInput());
            res.put("answer",paperService.getPaperAnswer(paper_id));
        }
        return res;
    }

    @Override
    public JSONArray selectPaperGrades(int paper_id) {
        List<Exam> exams = examDao.selectByPaperId(paper_id);
        JSONArray res = new JSONArray();
        for (Exam exam : exams) {
            JSONObject tmp = new JSONObject();
            tmp.put("score",exam.getScore());
            tmp.put("date",exam.getDate());
            User user = userDao.getById(exam.getUser_id());
            tmp.put("username",user.getUsername());
            tmp.put("nickname",user.getNickname());
            res.add(tmp);
        }
        return res;
    }


}
