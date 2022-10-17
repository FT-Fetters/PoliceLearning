package com.lyun.policelearning.dao.question;

import com.lyun.policelearning.dao.BaseDao;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.apache.commons.math3.analysis.function.Sin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SingleChoiceDao extends BaseDao<SingleChoice> {
    SingleChoice getById(int id);
    void newQuestion(@Param("singleChoice") SingleChoice singleChoice);
    void updateQuestion(@Param("singleChoice") SingleChoice singleChoice);
    void deleteQuestion(int id);
    List<SingleChoice> selectNumsById(int id, int num);
}
