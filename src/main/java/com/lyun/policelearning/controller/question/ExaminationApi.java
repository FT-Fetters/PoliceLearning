package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lyun.policelearning.entity.SimulationSettings;
import com.lyun.policelearning.service.ErrorBookService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.paper.SimulationService;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.service.question.SingleChoiceService;
import com.lyun.policelearning.utils.ExamUtils;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RequestMapping("/exam")
@RestController
public class ExaminationApi {
    public static JSONObject presentRes;
    public static double total;
    @Autowired
    SingleChoiceService singleChoiceService;

    @Autowired
    MultipleChoiceService multipleChoiceService;

    @Autowired
    ErrorBookService errorBookService;

    @Autowired
    UserService userService;

    @Autowired
    JudgmentService judgmentService;

    @Autowired
    SimulationService simulationService;

    @RequestMapping(value = "/getExam",method = RequestMethod.GET)
    public Object exam(HttpServletRequest request){
        SimulationSettings settings = simulationService.getSettings();
        List<JSONObject> singleChoiceList = ExamUtils.sampleSingle(singleChoiceService,settings.getSin_num());
        List<JSONObject> multipleChoiceList = ExamUtils.sampleMultiple(multipleChoiceService,settings.getMul_num());
        List<JSONObject> judgmentList = ExamUtils.sampleJudgment(judgmentService,settings.getJud_num());
        JSONObject res = new JSONObject();
        res.put("single",singleChoiceList);
        res.put("multiple",multipleChoiceList);
        res.put("judgment",judgmentList);
        LogUtils.log("get examination","get",true,request);
        //获取当前考试的结果集
        presentRes = res;
        return new ResultBody<>(true,200,res);
    }
    @RequestMapping(value = "/getExam/calculate",method = RequestMethod.POST)
    public Object calculate(@RequestBody JSONObject userAnswerInfos){
        JSONObject res = new JSONObject();
        JSONArray infos = userAnswerInfos.getJSONArray("infos");
        String js = infos.toJSONString();
        String username = userAnswerInfos.getString("username");
        int userId = userService.getByUsername(username).getId();
        List<JSONObject> infos1 = JSONObject.parseArray(js,JSONObject.class);
        int num = 0;
        num = num + JSONArrayToList(presentRes.getJSONArray("judgment")) + JSONArrayToList(presentRes.getJSONArray("single")) + JSONArrayToList(presentRes.getJSONArray("multiple"));
        double grade =(double)100/num;
        for(JSONObject userAnswerInfo  : infos1) {
            String userAnswer = userAnswerInfo.getString("userAnswer");
            Integer type = userAnswerInfo.getInteger("type");
            Integer id = userAnswerInfo.getInteger("id");
            if (userAnswer != null ) {
                if (type == 1) {
                    if (judgmentService.check(id, userAnswer)) {
                        total = total + grade;
                    }else {
                        errorBookService.save(userId,1,id);
                    }
                } else if (type == 2) {
                    if (singleChoiceService.check(id, userAnswer)) {
                        total = total + grade;
                    }else {
                        errorBookService.save(userId,2,id);
                    }
                } else {
                    String answer = multipleChoiceService.getAnswer(id);
                    if (answer.contains(userAnswer.toUpperCase()) | answer.contains(userAnswer) && !answer.equalsIgnoreCase(userAnswer)) {
                        total = total + grade / 2;
                        errorBookService.save(userId,3,id);
                    }
                    if (answer.equalsIgnoreCase(userAnswer)) {
                        total = total + grade;
                    }else {
                        errorBookService.save(userId,3,id);
                    }
                }
            }
        }
        double temp = total;
        total = 0;
        res.put("total",(int)temp);
        return new ResultBody<>(true,200,res);
    }


    public int JSONArrayToList(JSONArray jsonArray){
        String js = JSONObject.toJSONString(jsonArray, SerializerFeature.WriteClassName);
        List<JSONObject>  res = JSONObject.parseArray(js, JSONObject.class);
        return res.size();
    }
}
