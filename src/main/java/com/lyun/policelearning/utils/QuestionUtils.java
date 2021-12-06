package com.lyun.policelearning.utils;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.service.question.SingleChoiceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionUtils {

    public static List<JSONObject> sampleSingle(SingleChoiceService singleChoiceService,int num){
        List<SingleChoice> singleChoiceList = singleChoiceService.findAll();
        HashMap<Integer,String> map = new HashMap<>();
        List<JSONObject> res = new ArrayList<>();
        if (singleChoiceList.size() <= num){
            for (SingleChoice singleChoice : singleChoiceList) {
                JSONObject tmp = new JSONObject();
                tmp.put("id",singleChoice.getId());
                tmp.put("problem",singleChoice.getProblem());
                tmp.put("option_a",singleChoice.getOption_a());
                tmp.put("option_b",singleChoice.getOption_b());
                tmp.put("option_c",singleChoice.getOption_c());
                tmp.put("option_d",singleChoice.getOption_d());
                res.add(tmp);
            }
        }else {
            while (map.size() < num){
                int random = (int) (Math.random() * singleChoiceList.size());
                if (!map.containsKey(random)){
                    map.put(random,"");
                    SingleChoice singleChoice = singleChoiceList.get(random);
                    JSONObject tmp = new JSONObject();
                    tmp.put("id",singleChoice.getId());
                    tmp.put("problem",singleChoice.getProblem());
                    tmp.put("option_a",singleChoice.getOption_a());
                    tmp.put("option_b",singleChoice.getOption_b());
                    tmp.put("option_c",singleChoice.getOption_c());
                    tmp.put("option_d",singleChoice.getOption_d());
                    res.add(tmp);
                }
            }
        }
        return res;
    }

    public static List<JSONObject> sampleMultiple(MultipleChoiceService multipleChoiceService, int num){
        List<MultipleChoice> multipleChoiceList = multipleChoiceService.findAll();
        HashMap<Integer,String> map = new HashMap<>();
        List<JSONObject> res = new ArrayList<>();
        if (multipleChoiceList.size() <= num){
            for (MultipleChoice multipleChoice : multipleChoiceList) {
                JSONObject tmp = new JSONObject();
                tmp.put("id",multipleChoice.getId());
                tmp.put("problem",multipleChoice.getProblem());
                tmp.put("option_a",multipleChoice.getOption_a());
                tmp.put("option_b",multipleChoice.getOption_b());
                tmp.put("option_c",multipleChoice.getOption_c());
                tmp.put("option_d",multipleChoice.getOption_d());
                res.add(tmp);
            }
        }else {
            while (map.size() < num){
                int random = (int) (Math.random() * multipleChoiceList.size());
                if (!map.containsKey(random)){
                    map.put(random,"");
                    MultipleChoice multipleChoice = multipleChoiceList.get(random);
                    JSONObject tmp = new JSONObject();
                    tmp.put("id",multipleChoice.getId());
                    tmp.put("problem",multipleChoice.getProblem());
                    tmp.put("option_a",multipleChoice.getOption_a());
                    tmp.put("option_b",multipleChoice.getOption_b());
                    tmp.put("option_c",multipleChoice.getOption_c());
                    tmp.put("option_d",multipleChoice.getOption_d());
                    res.add(tmp);
                }
            }
        }
        return res;
    }

    public static List<JSONObject> sampleJudgment(JudgmentService judgmentService, int num){
        List<Judgment> judgmentList = judgmentService.findAll();
        HashMap<Integer,String> map = new HashMap<>();
        List<JSONObject> res = new ArrayList<>();
        if (judgmentList.size() <= num){
            for (Judgment judgment : judgmentList) {
                JSONObject tmp = new JSONObject();
                tmp.put("id",judgment.getId());
                tmp.put("problem",judgment.getProblem());
                tmp.put("option_true",judgment.getOption_true());
                tmp.put("option_false",judgment.getOption_false());
                res.add(tmp);
            }
        }else {
            while (map.size() < num){
                int random = (int) (Math.random() * judgmentList.size());
                if (!map.containsKey(random)){
                    map.put(random,"");
                    Judgment judgment = judgmentList.get(random);
                    JSONObject tmp = new JSONObject();
                    tmp.put("id",judgment.getId());
                    tmp.put("problem",judgment.getProblem());
                    tmp.put("option_true",judgment.getOption_true());
                    tmp.put("option_false",judgment.getOption_false());
                    res.add(tmp);
                }
            }
        }
        return res;
    }

}
