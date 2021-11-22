package com.lyun.policelearning.controller.course;

import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseApi {

    @Autowired
    CourseService courseService;

    @RequestMapping("/all")
    public Object getAll(){
        return new ResultBody<>(true,200,courseService.findAll());
    }
}
