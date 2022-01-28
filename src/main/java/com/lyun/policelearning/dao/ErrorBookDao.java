package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.ErrorBook;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.utils.UserUtils;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface ErrorBookDao extends BaseDao<ErrorBook>{
    void save(int userId, int type, int questionId, Date date);
    List<ErrorBook> findAllFromBook(int userId);
    Judgment findJudgmentById(int id);
    SingleChoice findSingleChoiceById(int id);
    MultipleChoice findMultipleChoiceById(int id);
    void delete(int userId,int type,int questionId);
    void deleteById(int type,int questionId);
}
