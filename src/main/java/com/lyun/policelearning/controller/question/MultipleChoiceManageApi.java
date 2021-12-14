package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/multipleChoice/manage")
@RestController
public class MultipleChoiceManageApi {

    @Autowired
    MultipleChoiceService multipleChoiceService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        if (UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
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
        if (answer.length() > 4){
            return new ResultBody<>(false,503,"error answer len");
        }
        MultipleChoice multipleChoice = new MultipleChoice();
        multipleChoice.setProblem(problem);
        multipleChoice.setOption_a(option_a);
        multipleChoice.setOption_b(option_b);
        multipleChoice.setOption_c(option_c);
        multipleChoice.setOption_d(option_d);
        multipleChoice.setAnswer(answer);
        multipleChoiceService.newQuestion(multipleChoice);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        if (UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        Integer id = data.getInteger("id");
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (multipleChoiceService.getById(id) == null){
            return new ResultBody<>(false,500,"id not found");
        }
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        MultipleChoice multipleChoice = multipleChoiceService.getById(id);
        multipleChoice.setId(id);
        multipleChoice.setProblem(problem);
        multipleChoice.setOption_a(option_a);
        multipleChoice.setOption_b(option_b);
        multipleChoice.setOption_c(option_c);
        multipleChoice.setOption_d(option_d);
        multipleChoice.setAnswer(answer);
        multipleChoiceService.updateQuestion(multipleChoice);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteQuestion(@RequestBody JSONObject data,HttpServletRequest request){
        if (UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        Integer id = data.getInteger("id");
        if (id == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        multipleChoiceService.deleteQuestion(id);
        return new ResultBody<>(true,200,null);
    }
}
