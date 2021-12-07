package com.lyun.policelearning.utils;

import com.alibaba.fastjson.JSONArray;
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
                tmp.put("type",1);
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
                    tmp.put("type",1);
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
                tmp.put("type",2);
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
                    tmp.put("type",2);
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
                JSONArray option = new JSONArray();
                JSONObject option_true = new JSONObject();
                option_true.put("id","A");
                option_true.put("content",judgment.getOption_true());
                option.add(option_true);
                JSONObject option_false = new JSONObject();
                option_true.put("id","B");
                option_true.put("content",judgment.getOption_false());
                option.add(option_false);
                tmp.put("option",option);
                tmp.put("type",3);
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
                    tmp.put("type",4);
                    res.add(tmp);
                }
            }
        }
        return res;
    }

}
