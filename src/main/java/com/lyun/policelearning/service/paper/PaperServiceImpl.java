package com.lyun.policelearning.service.paper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @Override
    public List<Paper> selectAll() {
        return paperDao.selectAll();
    }

    @Override
    public List<Paper> userSelectAll() {
        List<Paper> res = new ArrayList<>();
        for (Paper paper : paperDao.selectAll()) {
            if (paper.isEnable())res.add(paper);
        }
        return res;
    }

    @Override
    public int generate(int j, int m, int s,String title) {
        //新建试卷
        Paper paper = new Paper();
        paper.setCreateUser(1);
        paper.setTitle(title);
        paperDao.insert(paper);
        int id = paper.getId();
        //随机选择题目
        Random random = new Random(System.currentTimeMillis());
        //判断题
        List<Judgment> tj = judgmentDao.findAll();
        List<MultipleChoice> tm = multipleChoiceDao.findAll();
        List<SingleChoice> ts = singleChoiceDao.findAll();
        if(j > tj.size() || m > tm.size() || s > ts.size()){
            return -1;
        }
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
                        question.getJSONArray("j").add(judgment);
                        break;
                    case 'm':
                        MultipleChoice multipleChoice = multipleChoiceDao.getById(paperQuestion.getQuestion_id());
                        multipleChoice.setAnswer(null);
                        question.getJSONArray("m").add(multipleChoice);
                        break;
                    case 's':
                        SingleChoice singleChoice = singleChoiceDao.getById(paperQuestion.getQuestion_id());
                        singleChoice.setAnswer(null);
                        question.getJSONArray("s").add(singleChoice);
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
}
