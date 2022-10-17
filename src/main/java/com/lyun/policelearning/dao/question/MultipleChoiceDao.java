package com.lyun.policelearning.dao.question;


import com.lyun.policelearning.dao.BaseDao;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MultipleChoiceDao extends BaseDao<MultipleChoice> {
    MultipleChoice getById(int id);
    void newQuestion(@Param("multipleChoice") MultipleChoice multipleChoice);
    void updateQuestion(@Param("multipleChoice") MultipleChoice multipleChoice);
    void deleteQuestion(int id);
    String getAnswer(int id);
    List<MultipleChoice> selectNumsById(int id, int num);
}
