package com.lyun.policelearning.controller;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Course;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.service.LawService;
import com.lyun.policelearning.service.RuleService;
import com.lyun.policelearning.utils.ResultBody;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/count",method = RequestMethod.GET)
@RestController
public class CountApi {

    @Autowired
    CourseService courseService;

    @Autowired
    RuleService ruleService;

    @Autowired
    InformationService informationService;

    @Autowired
    LawService lawService;

    @RequestMapping("/course")
    public Object countCourse(){
        List<JSONObject> courseList = courseService.findAll(-1);
        return new ResultBody<>(true,200,courseList.size());
    }

//    @RequestMapping("/mock")
//    public Object countMockExam(){
//        List<MockEx>
//    }

    @RequestMapping("/rule")
    public Object countRule(){
        List<JSONObject> rules = ruleService.findAll();
        return new ResultBody<>(true,200,rules.size());
    }

    @SneakyThrows
    @RequestMapping("/info")
    public Object countInfo(){
        List<JSONObject> information = informationService.findAll();
        return new ResultBody<>(true,200,information.size());
    }

    @RequestMapping("/law")
    public Object countLaw(){
        List<JSONObject> laws = lawService.findAll();
        return new ResultBody<>(true,200,laws.size());

    }
}
