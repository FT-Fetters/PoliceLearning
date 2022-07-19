package com.lyun.policelearning.dao.paper;

import com.lyun.policelearning.entity.Exam;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ExamDao {

    int submit(int user_id, int paper_id, Date date, float score,String input);
    Exam selectByUserIdAndPaperId(int user_id,int paper_id);
    List<Exam> selectByUserId(int user_id);
}
