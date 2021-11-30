package com.lyun.policelearning.controller.question;

import com.lyun.policelearning.dao.question.MultipleChoiceDao;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/multipleChoice")
@RestController
public class MultipleChoiceApi {

    @Autowired
    MultipleChoiceService multipleChoiceService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object list(){
        List<MultipleChoice> multipleChoices = multipleChoiceService.findAll();
        List<Integer> multipleChoiceId = new ArrayList<>();
        for (MultipleChoice multipleChoice : multipleChoices) {
            multipleChoiceId.add(multipleChoice.getId());
        }
        return new ResultBody<>(true,200,multipleChoiceId);
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object get(@RequestParam int id){
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        MultipleChoice res = multipleChoiceService.getById(id);
        if (res == null){
            return new ResultBody<>(false,501,"id not found");
        }
        return new ResultBody<>(true,200,res);
    }

}
