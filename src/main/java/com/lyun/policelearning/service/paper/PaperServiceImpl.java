package com.lyun.policelearning.service.paper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.paper.ExamDao;
import com.lyun.policelearning.dao.paper.PaperDao;
import com.lyun.policelearning.dao.paper.PaperQuestionDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.Paper;
import com.lyun.policelearning.entity.PaperQuestion;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PaperServiceImpl implements PaperService{

    @Autowired
    private PaperDao paperDao;

    @Autowired
    private PaperQuestionDao paperQuestionDao;

    @Autowired
    private JudgmentDao judgmentDao;

    @Autowired
    private MultipleChoiceDao multipleChoiceDao;

    @Autowired
    private SingleChoiceDao singleChoiceDao;

    @Autowired
    private ExamDao examDao;

    @Override
    public List<Paper> selectAll() {
        return paperDao.selectAll();
    }

    @Override
    public JSONArray userSelectAll(int userId) {
        JSONArray res = new JSONArray();
        for (Paper paper : paperDao.selectAll()) {
            JSONObject tmp = ((JSONObject) JSONObject.toJSON(paper));
            if (examDao.selectByUserIdAndPaperId(userId,paper.getId()) == null)
                tmp.put("finish",false);
            else
                tmp.put("finish",true);
            res.add(tmp);
        }
        return res;
    }

    @Override
    public int generate(int j, int m, int s,String title) {
        //新建试卷
        Paper paper = new Paper();
        paper.setCreateUser(1);
        paper.setTitle(title);
        paper.setDate(new Date(System.currentTimeMillis()));
        //随机选择题目
        Random random = new Random(System.currentTimeMillis());
        //判断题
        List<Judgment> tj = judgmentDao.findAll();
        List<MultipleChoice> tm = multipleChoiceDao.findAll();
        List<SingleChoice> ts = singleChoiceDao.findAll();
        if(j > tj.size() || m > tm.size() || s > ts.size()){
            return -1;
        }
        paperDao.insert(paper);
        int id = paper.getId();
        int index = 0;
        for (int i = 0; i < j; i++) {
            int k = random.nextInt(tj.size());
            Judgment judgment = tj.get(k);
            PaperQuestion q = new PaperQuestion();
            q.setQuestion_id(judgment.getId());
            q.setType('j');
            q.setPaper_id(id);
            q.setIndex(index);
            index++;
            paperQuestionDao.insert(q);
            tj.remove(k);
        }
        //多选题
        for (int i = 0; i < m; i++) {
            int k = random.nextInt(tm.size());
            MultipleChoice multipleChoice = tm.get(k);
            PaperQuestion q = new PaperQuestion();
            q.setType('m');
            q.setPaper_id(id);
            q.setQuestion_id(multipleChoice.getId());
            q.setIndex(index);
            index++;
            paperQuestionDao.insert(q);
            tm.remove(k);
        }
        //单选题
        for (int i = 0; i < s; i++) {
            int k = random.nextInt(ts.size());
            SingleChoice singleChoice = ts.get(k);
            PaperQuestion q = new PaperQuestion();
            q.setType('s');
            q.setPaper_id(id);
            q.setQuestion_id(singleChoice.getId());
            q.setIndex(index);
            index++;
            paperQuestionDao.insert(q);
            ts.remove(k);
        }
        return id;
    }

    @Override
    public JSONObject getById(int id) {
        JSONObject res = new JSONObject();
        Paper paper = paperDao.getById(id);
        res.put("id",paper.getId());
        res.put("title",paper.getTitle());
        JSONObject question = new JSONObject();
        List<PaperQuestion> paperQuestions = paperQuestionDao.selectByPaperId(paper.getId());
        question.put("j",new JSONArray());
        question.put("m",new JSONArray());
        question.put("s",new JSONArray());
        for (PaperQuestion paperQuestion : paperQuestions) {
            switch (paperQuestion.getType()){
                case 'j':
                    question.getJSONArray("j").add(judgmentDao.getById(paperQuestion.getQuestion_id()));
                    break;
                case 'm':
                    question.getJSONArray("m").add(multipleChoiceDao.getById(paperQuestion.getQuestion_id()));
                    break;
                case 's':
                    question.getJSONArray("s").add(singleChoiceDao.getById(paperQuestion.getQuestion_id()));
            }
        }
        res.put("question",question);
        return res;
    }

    @Override
    public JSONObject userGetById(int id) {
        if (paperDao.getById(id).isEnable()){
            JSONObject res = new JSONObject();
            Paper paper = paperDao.getById(id);
            res.put("id",paper.getId());
            res.put("title",paper.getTitle());
            JSONObject question = new JSONObject();
            List<PaperQuestion> paperQuestions = paperQuestionDao.selectByPaperId(paper.getId());
            question.put("j",new JSONArray());
            question.put("m",new JSONArray());
            question.put("s",new JSONArray());
            for (PaperQuestion paperQuestion : paperQuestions) {
                switch (paperQuestion.getType()){
                    case 'j':
                        Judgment judgment = judgmentDao.getById(paperQuestion.getQuestion_id());
                        judgment.setAnswer(null);
                        JSONObject tmp = new JSONObject();
                        tmp.put("index",paperQuestion.getIndex());
                        tmp.put("id",judgment.getId());
                        tmp.put("problem",judgment.getProblem());
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
                        question.getJSONArray("j").add(tmp);
                        break;
                    case 'm':
                        MultipleChoice multipleChoice = multipleChoiceDao.getById(paperQuestion.getQuestion_id());
                        multipleChoice.setAnswer(null);
                        tmp = new JSONObject();
                        tmp.put("id",multipleChoice.getId());
                        tmp.put("problem",multipleChoice.getProblem());
                        option = new JSONArray();
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
                        tmp.put("index",paperQuestion.getIndex());
                        question.getJSONArray("m").add(tmp);
                        break;
                    case 's':
                        SingleChoice singleChoice = singleChoiceDao.getById(paperQuestion.getQuestion_id());
                        singleChoice.setAnswer(null);
                        tmp = new JSONObject();
                        tmp.put("id",singleChoice.getId());
                        tmp.put("problem",singleChoice.getProblem());
                        option = new JSONArray();
                        option_a = new JSONObject();
                        option_a.put("id","A");
                        option_a.put("content",singleChoice.getOption_a());
                        option.add(option_a);
                        option_b = new JSONObject();
                        option_b.put("id","B");
                        option_b.put("content",singleChoice.getOption_b());
                        option.add(option_b);
                        option_c = new JSONObject();
                        option_c.put("id","C");
                        option_c.put("content",singleChoice.getOption_c());
                        option.add(option_c);
                        option_d = new JSONObject();
                        option_d.put("id","D");
                        option_d.put("content",singleChoice.getOption_d());
                        option.add(option_d);
                        tmp.put("option",option);
                        tmp.put("index",paperQuestion.getIndex());
                        question.getJSONArray("s").add(tmp);
                        break;
                }
            }
            res.put("question",question);
            return res;
        }else return null;
    }

    @Override
    public boolean delete(int id) {
        if (paperDao.getById(id) != null){
            paperDao.delete(id);
            paperQuestionDao.deleteByPaperId(id);
            return true;
        }else return false;
    }

    @Override
    public boolean enable(int id) {
        if (paperDao.getById(id)==null)return false;
        paperDao.setState(id, 1);
        return true;
    }

    @Override
    public boolean disable(int id) {
        if (paperDao.getById(id)==null)return false;
        paperDao.setState(id, 0);
        return true;
    }

    @Override
    public String getPaperAnswer(int id) {
        List<PaperQuestion> paperQuestions = paperQuestionDao.selectByPaperId(id);
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < paperQuestions.size(); i++) {
            PaperQuestion paperQuestion = paperQuestions.get(i);
            if (paperQuestion.getType() == 'j'){
                Judgment judgment = judgmentDao.getById(paperQuestion.getQuestion_id());
                res.append(judgment.getAnswer()).append(i==paperQuestions.size()-1?"":",");
            }else if (paperQuestion.getType() == 'm'){
                MultipleChoice multipleChoice = multipleChoiceDao.getById(paperQuestion.getQuestion_id());
                res.append(multipleChoice.getAnswer()).append(i==paperQuestions.size()-1?"":",");
            }else if (paperQuestion.getType() == 's'){
                SingleChoice singleChoice = singleChoiceDao.getById(paperQuestion.getQuestion_id());
                res.append(singleChoice.getAnswer()).append(i==paperQuestions.size()-1?"":",");
            }
        }
        return res.toString();
    }
}
