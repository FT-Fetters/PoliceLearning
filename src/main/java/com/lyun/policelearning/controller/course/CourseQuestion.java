package com.lyun.policelearning.controller.course;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course/question")
public class CourseQuestion {

    @GetMapping("/get")
    public Object getCourseQuestion(int contentId){
        return null;
    }


}
