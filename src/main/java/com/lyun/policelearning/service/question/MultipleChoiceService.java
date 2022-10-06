package com.lyun.policelearning.service.question;

import com.alibaba.fastjson.JSONArray;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface MultipleChoiceService {
    List<MultipleChoice> findAll();
    MultipleChoice getById(int id);
    void newQuestion(MultipleChoice multipleChoice);
    void updateQuestion(MultipleChoice multipleChoice);
    void deleteQuestion(int id);
    boolean check(int id,String answer);
    String getAnswer(int id);
    int importQuestion(List<MultipleChoice> multipleChoices);
    PageResult selectByPage(PageRequest pageRequest);
    void batchDelete(JSONArray list);
}
