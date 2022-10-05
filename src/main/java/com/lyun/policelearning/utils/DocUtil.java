package com.lyun.policelearning.utils;

import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;

import java.util.List;

public interface DocUtil {

    List<Judgment> getJudgments(String filename);

    List<SingleChoice> getSingleChoice(String filename);

    List<MultipleChoice> getMultipleChoice(String filename);

}
