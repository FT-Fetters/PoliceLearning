package com.lyun.policelearning.service.question;

import com.alibaba.fastjson.JSONArray;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface JudgmentService {
    List<Judgment> findAll();
    Judgment getById(int id);
    void newQuestion(Judgment judgment);
    void updateQuestion(Judgment judgment);
    void deleteQuestion(int id);
    boolean check(int id,String answer);
    int importQuestion(List<Judgment> judgments);
    PageResult selectByPage(PageRequest pageRequest);
    void batchDelete(JSONArray list);
}
