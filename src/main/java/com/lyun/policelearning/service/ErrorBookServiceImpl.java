package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.ErrorBookDao;
import com.lyun.policelearning.entity.ErrorBook;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorBookServiceImpl implements ErrorBookService{
    @Autowired
    ErrorBookDao errorBookDao;
    @Override
    public void save(int userId, int type, int questionId) {
        Date date = new Date(System.currentTimeMillis());
        errorBookDao.save(userId,type,questionId,date);
    }

    @Override
    public List<JSONObject> findAll(int userId) {
        List<JSONObject> jsonObjects = new ArrayList<>();
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
        return jsonObjects;
    }

    @Override
    public void delete(int userId, int type, int questionId) {
        errorBookDao.delete(userId,type,questionId);
    }
}
