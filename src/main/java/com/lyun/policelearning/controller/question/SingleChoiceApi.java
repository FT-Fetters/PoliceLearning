package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.question.SingleChoiceService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/singleChoice")
public class SingleChoiceApi {

    @Autowired
    SingleChoiceService singleChoiceService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object list(){
        List<SingleChoice> singleChoices = singleChoiceService.findAll();
        List<Integer> singleChoiceId = new ArrayList<>();
        for (SingleChoice singleChoice : singleChoices) {
            singleChoiceId.add(singleChoice.getId());
        }
        return new ResultBody<>(true,200,singleChoiceId);
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object get(@RequestParam int id){
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        SingleChoice res = singleChoiceService.getById(id);
        if (res == null){
            return new ResultBody<>(false,501,"id not found");
        }
        return new ResultBody<>(true,200,res);
    }

    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Object check(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId == -1){
            return new ResultBody<>(false,500,"not login");
        }
        Integer id = data.getInteger("id");
        String answer = data.getString("answer");
        if (id == null || answer == null){
            return new ResultBody<>(false,501,"missing parameter");
        }
        if (singleChoiceService.check(id,answer)){
            return new ResultBody<>(true,200,true);
        }else return new ResultBody<>(true,200,false);
    }
}
