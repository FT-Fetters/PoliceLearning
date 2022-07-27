package com.lyun.policelearning.service.question;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;

import java.util.List;

public interface SingleChoiceService {
    List<SingleChoice> findAll();
    SingleChoice getById(int id);
    void newQuestion(SingleChoice singleChoice);
    void updateQuestion(SingleChoice singleChoice);
    void deleteQuestion(int id);
    boolean check(int id,String answer);
    int importQuestion(List<SingleChoice> singleChoices);

}
