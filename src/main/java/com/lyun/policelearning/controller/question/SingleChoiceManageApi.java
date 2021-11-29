package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.SingleChoiceService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/singleChoice/manage")
@RestController
public class SingleChoiceManageApi {

    @Autowired
    SingleChoiceService singleChoiceService;

    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        SingleChoice singleChoice = new SingleChoice();
        singleChoice.setProblem(problem);
        singleChoice.setOption_a(option_a);
        singleChoice.setOption_b(option_b);
        singleChoice.setOption_c(option_c);
        singleChoice.setOption_d(option_d);
        singleChoice.setAnswer(answer);
        singleChoiceService.newQuestion(singleChoice);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping("/update")
    public Object updateQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        Integer id = data.getInteger("id");
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (singleChoiceService.getById(id) == null){
            return new ResultBody<>(false,500,"id not found");
        }
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        SingleChoice singleChoice = singleChoiceService.getById(id);
        singleChoice.setId(id);
        singleChoice.setProblem(problem);
        singleChoice.setOption_a(option_a);
        singleChoice.setOption_b(option_b);
        singleChoice.setOption_c(option_c);
        singleChoice.setOption_d(option_d);
        singleChoice.setAnswer(answer);
        singleChoiceService.updateQuestion(singleChoice);
        return new ResultBody<>(true,200,null);
    }

}
