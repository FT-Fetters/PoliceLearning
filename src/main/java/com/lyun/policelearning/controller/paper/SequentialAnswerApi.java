package com.lyun.policelearning.controller.paper;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.controller.paper.model.SequentialGetBody;
import com.lyun.policelearning.service.question.AnswerProgressService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sequential")
public class SequentialAnswerApi {

    @Autowired
    private AnswerProgressService answerProgressService;

    @Autowired
    private JwtConfig jwtConfig;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object getQuestion(@RequestBody SequentialGetBody body, HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        body.setUserId(userId);
        JSONObject res =answerProgressService.getQuestion(body);
        return new ResultBody<>(true,200,res);
    }



}
