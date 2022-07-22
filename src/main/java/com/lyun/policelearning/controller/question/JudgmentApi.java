package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.apache.catalina.webresources.JarWarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/judgment")
@RestController
public class JudgmentApi {

    @Autowired
    JudgmentService judgmentService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object list(){
        List<Judgment> judgments = judgmentService.findAll();
        List<Integer> judgmentsId = new ArrayList<>();
        for (Judgment judgment : judgments) {
            judgmentsId.add(judgment.getId());
        }
        return new ResultBody<>(true,200,judgmentsId);
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object get(@RequestParam int id){
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        Judgment res = judgmentService.getById(id);
        if (res == null){
            return new ResultBody<>(false,501,"id not found");
        }
        return new ResultBody<>(true,200,res);
    }

    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Object check(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
        Integer id = data.getInteger("id");
        String answer = data.getString("answer");
        if (id == null || answer == null){
            return new ResultBody<>(false,501,"missing parameter");
        }
        if (judgmentService.check(id,answer)){
            return new ResultBody<>(true,200,true);
        }else return new ResultBody<>(true,200,false);
    }
}
