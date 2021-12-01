package com.lyun.policelearning.service.question;

import com.lyun.policelearning.entity.question.Judgment;

import java.util.List;

public interface JudgmentService {
    List<Judgment> findAll();
    Judgment getById(int id);
    void newQuestion(Judgment judgment);
    void updateQuestion(Judgment judgment);
    void deleteQuestion(int id);
}
