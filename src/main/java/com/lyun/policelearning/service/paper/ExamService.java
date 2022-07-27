package com.lyun.policelearning.service.paper;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Exam;

import java.util.List;

public interface ExamService {

    float submit(int userId, int paperId, List<String> inputs);
    List<Exam> selectByUserId(int user_id);
    JSONObject getExamScore(int paper_id, int user_id);
}
