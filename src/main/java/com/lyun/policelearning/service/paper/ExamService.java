package com.lyun.policelearning.service.paper;

import com.lyun.policelearning.entity.Exam;

import java.util.List;

public interface ExamService {

    float submit(int userId, int paperId, List<String> inputs);
    List<Exam> selectByUserId(int user_id);
}
