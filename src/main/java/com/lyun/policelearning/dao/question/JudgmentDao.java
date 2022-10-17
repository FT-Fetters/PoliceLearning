package com.lyun.policelearning.dao.question;

import com.lyun.policelearning.dao.BaseDao;
import com.lyun.policelearning.entity.question.Judgment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JudgmentDao extends BaseDao<Judgment> {
    Judgment getById(int id);
    void newQuestion(@Param("judgment") Judgment judgment);
    void updateQuestion(@Param("judgment") Judgment judgment);
    void deleteQuestion(int id);
    List<Judgment> selectNumsById(int id, int num);
}
