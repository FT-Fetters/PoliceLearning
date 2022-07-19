package com.lyun.policelearning.service.paper;

import com.lyun.policelearning.dao.paper.ExamDao;
import com.lyun.policelearning.dao.paper.PaperDao;
import com.lyun.policelearning.dao.paper.PaperQuestionDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.Exam;
import com.lyun.policelearning.entity.PaperQuestion;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService{

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
        for (String s : inputs) {
            input.append(s);
        }
        examDao.submit(userId,paperId,new Date(System.currentTimeMillis()),sumScore,input.toString());
        return sumScore;
    }

    @Override
    public List<Exam> selectByUserId(int user_id) {
        return examDao.selectByUserId(user_id);
    }

}
