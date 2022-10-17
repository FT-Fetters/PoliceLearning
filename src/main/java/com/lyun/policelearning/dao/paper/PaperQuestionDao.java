package com.lyun.policelearning.dao.paper;

import com.lyun.policelearning.entity.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaperQuestionDao {
    void insert(PaperQuestion paperQuestion);
    List<PaperQuestion> selectByPaperId(int paperId);
    void deleteByPaperId(int id);
    void deleteByIndexId(PaperQuestion paperQuestion);
}
