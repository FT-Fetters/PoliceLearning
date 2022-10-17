package com.lyun.policelearning.dao.question;

import com.lyun.policelearning.entity.AnswerProgress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnswerProgressDao {
    AnswerProgress getByUserId(int userId);
    void update(AnswerProgress answerProgress);
    void delete(int id);
    void insert(AnswerProgress answerProgress);
    void select(int id);
}
