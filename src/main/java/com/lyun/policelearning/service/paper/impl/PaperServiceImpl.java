package com.lyun.policelearning.service.paper.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.controller.paper.model.GeneratePaperBody;
import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.dao.paper.ExamDao;
import com.lyun.policelearning.dao.paper.PaperDao;
import com.lyun.policelearning.dao.paper.PaperQuestionDao;
import com.lyun.policelearning.dao.question.JudgmentDao;
import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.Exam;
import com.lyun.policelearning.entity.Paper;
import com.lyun.policelearning.entity.PaperQuestion;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import com.lyun.policelearning.service.paper.PaperService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PaperServiceImpl implements PaperService {

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

    @Autowired
    UserDao userDao;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public static Page page;

    @Override
    public PageResult selectAll(PageRequest pageRequest) {
        //将pageInfo传递到getPageResult中获得result结果，而pageInfo又是与数据库中的数据有关的
        return PageUtil.getPageResult(getPageInfo(pageRequest),page);
    }

    @Override
    public JSONArray userSelectAll(int userId) {
        JSONArray res = new JSONArray();
        for (Paper paper : paperDao.selectAll()) {
            if(paper.isEnable()){
                JSONObject tmp = ((JSONObject) JSONObject.toJSON(paper));
                if (examDao.selectByUserIdAndPaperId(userId,paper.getId()) == null) {
                    tmp.put("finish", false);
                }
                else {
                    tmp.put("finish", true);
                }
                res.add(tmp);
            }
        }
        return res;
    }

    @Override
    public int generate(GeneratePaperBody body) {
        //新建试卷
        Paper paper = new Paper();
        paper.setCreateUser(body.getUid());
        paper.setTitle(body.getTitle());
        paper.setScore("" + body.getScore_j() + "," + body.getScore_s() + "," + body.getScore_m());
        Integer j = body.getJ();
        Integer s = body.getS();
        Integer m = body.getM();
        paper.setDate(new Date(System.currentTimeMillis()));
        paper.setTime(body.getTime());
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
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
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
        session.commit();
        return id;
    }

    @Override
    public JSONObject getById(int id) {
        JSONObject res = new JSONObject();
        Paper paper = paperDao.getById(id);
        res.put("id",paper.getId());
        res.put("title",paper.getTitle());
        res.put("time",paper.getTime());
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
            res.put("time",paper.getTime());
            JSONObject question = new JSONObject();
            List<PaperQuestion> paperQuestions = paperQuestionDao.selectByPaperId(paper.getId());
            question.put("j",new JSONArray());
            question.put("m",new JSONArray());
            question.put("s",new JSONArray());
            for (PaperQuestion paperQuestion : paperQuestions) {
                switch (paperQuestion.getType()){
                    case 'j':
                        Judgment judgment = judgmentDao.getById(paperQuestion.getQuestion_id());
                        if (judgment == null){
                            judgment = new Judgment();
                            judgment.setProblem("该问题不存在");
                        }
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
                        if (multipleChoice == null){
                            multipleChoice = new MultipleChoice();
                            multipleChoice.setProblem("该问题不存在");
                        }

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
                        if (singleChoice == null){
                            singleChoice = new SingleChoice();
                            singleChoice.setProblem("该问题不存在");
                        }

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
                if (judgment == null)
                    judgment = new Judgment();
                res.append(judgment.getAnswer()).append(i==paperQuestions.size()-1?"":",");
            }else if (paperQuestion.getType() == 'm'){
                MultipleChoice multipleChoice = multipleChoiceDao.getById(paperQuestion.getQuestion_id());
                if (multipleChoice == null)
                    multipleChoice = new MultipleChoice();
                res.append(multipleChoice.getAnswer()).append(i==paperQuestions.size()-1?"":",");
            }else if (paperQuestion.getType() == 's'){
                SingleChoice singleChoice = singleChoiceDao.getById(paperQuestion.getQuestion_id());
                if (singleChoice == null)
                    singleChoice = new SingleChoice();
                res.append(singleChoice.getAnswer()).append(i==paperQuestions.size()-1?"":",");
            }
        }
        return res.toString();
    }

    @Override
    public int count() {
        return paperDao.selectAll().size();
    }

    @Override
    public List<JSONObject> getHistory(int uid) {
        List<JSONObject> res = new ArrayList<>();
        for (Exam exam : examDao.selectByUserId(uid)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",exam.getId());
            jsonObject.put("date",exam.getDate());
            if (paperDao.getById(exam.getPaper_id()) != null){
                jsonObject.put("title",paperDao.getById(exam.getPaper_id()).getTitle());
            }else {
                jsonObject.put("title",null);
            }
            jsonObject.put("score",exam.getScore());
            res.add(jsonObject);
        }
        return res;
    }

    @Override
    public void paperAddQue(Integer paperId, String type, Integer queId) {
        List<PaperQuestion> paperQuestions = paperQuestionDao.selectByPaperId(paperId);
        paperQuestions.sort(Comparator.comparing(PaperQuestion::getIndex));
        int index = paperQuestions.get(paperQuestions.size() - 1).getIndex() + 1;
        if (type.equals("j") || type.equals("s") || type.equals("m")){
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaper_id(paperId);
            paperQuestion.setQuestion_id(queId);
            paperQuestion.setIndex(index);
            switch (type.charAt(0)){
                case 'j':
                    paperQuestion.setType('j');
                    paperQuestionDao.insert(paperQuestion);
                    break;
                case 's':
                    paperQuestion.setType('s');
                    paperQuestionDao.insert(paperQuestion);
                    break;
                case 'm':
                    paperQuestion.setType('m');
                    paperQuestionDao.insert(paperQuestion);
            }
        }
    }

    @Override
    public void paperDelQue(Integer paperId, String type, Integer index) {
        PaperQuestion paperQuestion = new PaperQuestion();
        paperQuestion.setPaper_id(paperId);
        paperQuestion.setIndex(index);
        paperQuestion.setType(type.charAt(0));
        paperQuestionDao.deleteByIndexId(paperQuestion);
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Paper paper : paperDao.selectAll()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",paper.getId());
            if (userDao.getById(paper.getCreateUser()).getUsername() != null){
                jsonObject.put("createUser",userDao.getById(paper.getCreateUser()).getUsername());
            }else {
                jsonObject.put("creatUser",null);
            }
            jsonObject.put("date",paper.getDate());
            jsonObject.put("title",paper.getTitle());
            jsonObject.put("enable",paper.isEnable());
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }


}
