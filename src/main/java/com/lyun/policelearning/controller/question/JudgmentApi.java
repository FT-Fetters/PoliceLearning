package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/judgment")
@RestController
public class JudgmentApi {

    @Autowired
    JudgmentService judgmentService;


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





}
