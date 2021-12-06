package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.service.question.SingleChoiceService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.QuestionUtils;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/mock")
@RestController
public class MockExaminationApi {


    @Autowired
    SingleChoiceService singleChoiceService;

    @Autowired
    MultipleChoiceService multipleChoiceService;

    @Autowired
    JudgmentService judgmentService;

    @RequestMapping("/sample")
    public Object sample(@RequestParam int single, @RequestParam int multiple, @RequestParam int judgment, HttpServletRequest request){
        List<JSONObject> singleChoiceList = QuestionUtils.sampleSingle(singleChoiceService,single);
        List<JSONObject> multipleChoiceList = QuestionUtils.sampleMultiple(multipleChoiceService,multiple);
        List<JSONObject> judgmentList = QuestionUtils.sampleJudgment(judgmentService,judgment);
        JSONObject res = new JSONObject();
        res.put("single",singleChoiceList);
        res.put("multiple",multipleChoiceList);
        res.put("judgment",judgmentList);
        LogUtils.log("get mock examination","get",true,request);
        return new ResultBody<>(true,200,res);
    }
}
