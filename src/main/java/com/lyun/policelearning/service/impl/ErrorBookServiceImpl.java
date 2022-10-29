package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.entity.ErrorBook;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.ErrorBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorBookServiceImpl implements ErrorBookService {
    @Autowired
    ErrorBookDao errorBookDao;
    @Override
    public boolean save(int userId, int type, int questionId) {
        if(errorBookDao.check(userId, type, questionId) != null){
            return false;
        }
        Date date = new Date(System.currentTimeMillis());
        errorBookDao.save(userId,type,questionId,date);
        return true;
    }

    @Override
    public JSONObject findAll(int userId) {
        JSONObject res = new JSONObject();
        List<JSONObject> judgementArray = new ArrayList<>();
        List<JSONObject> singleArray = new ArrayList<>();
        List<JSONObject> multipleArray = new ArrayList<>();

        for (ErrorBook errorBook : errorBookDao.findAllFromBook(userId)){
            //判断题查询
            if (errorBook.getType() == 1){
                Judgment judgment = errorBookDao.findJudgmentById(errorBook.getQuestionId());
                JSONObject jsonObject = new JSONObject();
                if (judgment != null){
                    jsonObject.put("id",judgment.getId());
                    jsonObject.put("type",1);
                    List<JSONObject> options = new ArrayList<>();
                    //返回选项
                    JSONObject option1 = new JSONObject();
                    option1.put("id","A");
                    option1.put("content",judgment.getOption_true());
                    JSONObject option2 = new JSONObject();
                    option2.put("id","B");
                    option2.put("content",judgment.getOption_false());
                    options.add(option1);
                    options.add(option2);
                    jsonObject.put("option",options);
                    jsonObject.put("answer",judgment.getAnswer());
                    jsonObject.put("problem",judgment.getProblem());
                    judgementArray.add(jsonObject);
                }
            }
            //单选题查询
            if (errorBook.getType() == 2){
                SingleChoice singleChoice = errorBookDao.findSingleChoiceById(errorBook.getQuestionId());
                JSONObject jsonObject = new JSONObject();
                if (singleChoice != null){
                    jsonObject.put("id",singleChoice.getId());
                    jsonObject.put("type",2);
                    List<JSONObject> options = new ArrayList<>();
                    //返回选项
                    JSONObject option1 = new JSONObject();
                    option1.put("id","A");
                    option1.put("content",singleChoice.getOption_a());
                    JSONObject option2 = new JSONObject();
                    option2.put("id","B");
                    option2.put("content",singleChoice.getOption_b());
                    JSONObject option3 = new JSONObject();
                    option3.put("id","C");
                    option3.put("content",singleChoice.getOption_c());
                    JSONObject option4 = new JSONObject();
                    option4.put("id","D");
                    option4.put("content",singleChoice.getOption_d());
                    options.add(option1);
                    options.add(option2);
                    options.add(option3);
                    options.add(option4);
                    jsonObject.put("option",options);

                    jsonObject.put("answer",singleChoice.getAnswer());
                    jsonObject.put("problem",singleChoice.getProblem());
                    singleArray.add(jsonObject);
                }
            }
            //多选题查询
            if (errorBook.getType() == 3){
                MultipleChoice multipleChoice = errorBookDao.findMultipleChoiceById(errorBook.getQuestionId());
                JSONObject jsonObject = new JSONObject();
                if (multipleChoice != null){
                    jsonObject.put("id",multipleChoice.getId());
                    jsonObject.put("type",3);
                    List<JSONObject> options = new ArrayList<>();
                    //返回选项
                    JSONObject option1 = new JSONObject();
                    option1.put("id","A");
                    option1.put("content",multipleChoice.getOption_a());
                    JSONObject option2 = new JSONObject();
                    option2.put("id","B");
                    option2.put("content",multipleChoice.getOption_b());
                    JSONObject option3 = new JSONObject();
                    option3.put("id","C");
                    option3.put("content",multipleChoice.getOption_c());
                    JSONObject option4 = new JSONObject();
                    option4.put("id","D");
                    option4.put("content",multipleChoice.getOption_d());
                    options.add(option1);
                    options.add(option2);
                    options.add(option3);
                    options.add(option4);
                    jsonObject.put("option",options);

                    jsonObject.put("answer",multipleChoice.getAnswer());
                    jsonObject.put("problem",multipleChoice.getProblem());
                    multipleArray.add(jsonObject);
                }
            }
        }
        res.put("judgement",judgementArray);
        res.put("single",singleArray);
        res.put("multiple",multipleArray);
        return res;

        /*List<JSONObject> jsonObjects = new ArrayList<>();
        Map<Integer, List<Integer>> res1 = new HashMap<>();
        List<Integer> judgement = new ArrayList<>();
        List<Integer> single = new ArrayList<>();
        List<Integer> multiple = new ArrayList<>();
        for (ErrorBook errorBook : errorBookDao.findAllFromBook(userId)) {
            if (errorBook.getType() == 1) {
                judgement.add(errorBook.getQuestionId());
            }
            if (errorBook.getType() == 2) {
                single.add(errorBook.getQuestionId());
            }
            if (errorBook.getType() == 3) {
                multiple.add(errorBook.getQuestionId());
            }
        }
        res1.put(1, judgement);
        res1.put(2, single);
        res1.put(3, multiple);
        List<Judgment> judgments = new ArrayList<>();
        List<SingleChoice> singleChoices = new ArrayList<>();
        List<MultipleChoice> multipleChoices = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : res1.entrySet()){
            if(entry.getKey() == 1){
                //查询判断题
                for(Integer questionId : entry.getValue()){
                    judgments.add(errorBookDao.findJudgmentById(questionId));
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("judgement",judgments);
                jsonObjects.add(jsonObject);
            }
            if(entry.getKey() == 2){
                for(Integer questionId : entry.getValue()){
                    singleChoices.add(errorBookDao.findSingleChoiceById(questionId));
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("singleChoice",singleChoices);
                jsonObjects.add(jsonObject);
            }
            if(entry.getKey() == 3){
                for(Integer questionId : entry.getValue()){
                    multipleChoices.add(errorBookDao.findMultipleChoiceById(questionId));
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("multipleChoice", multipleChoices);
                jsonObjects.add(jsonObject);
            }
        }
        return jsonObjects;*/
    }

    @Override
    public void delete(int userId, int type, int questionId) {
        errorBookDao.delete(userId,type,questionId);
    }
    /**
     * 根据
     */
}
