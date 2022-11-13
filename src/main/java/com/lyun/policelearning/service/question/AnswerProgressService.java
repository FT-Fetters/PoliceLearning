package com.lyun.policelearning.service.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.controller.paper.model.ResetBody;
import com.lyun.policelearning.controller.paper.model.SequentialGetBody;

public interface AnswerProgressService {

    JSONObject getQuestion(SequentialGetBody body);

    JSONObject getProgress(int userId);

    void reset(ResetBody body);

}
