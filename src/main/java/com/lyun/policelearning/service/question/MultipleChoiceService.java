package com.lyun.policelearning.service.question;

import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;

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
}
