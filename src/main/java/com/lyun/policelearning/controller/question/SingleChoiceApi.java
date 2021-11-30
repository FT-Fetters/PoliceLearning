package com.lyun.policelearning.controller.question;

import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.SingleChoiceService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/singleChoice")
public class SingleChoiceApi {

    @Autowired
    SingleChoiceService singleChoiceService;

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
}
