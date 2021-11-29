package com.lyun.policelearning.dao.question;

import com.lyun.policelearning.dao.BaseDao;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SingleChoiceDao extends BaseDao<SingleChoice> {
    SingleChoice getById(int id);
    void newQuestion(@Param("singleChoice") SingleChoice singleChoice);
    void updateQuestion(@Param("singleChoice") SingleChoice singleChoice);
}
