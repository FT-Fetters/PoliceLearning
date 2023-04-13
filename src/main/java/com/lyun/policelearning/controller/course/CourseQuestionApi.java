package com.lyun.policelearning.controller.course;


import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.course.CourseQuestion;
import com.lyun.policelearning.service.CourseQuestionService;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/course/question")
public class CourseQuestionApi {

    @Autowired
    private CourseQuestionService courseQuestionService;

    @Autowired
    private JwtConfig jwtConfig;

    @GetMapping("/get")
    public Object getCourseQuestion(int contentId){
        return courseQuestionService.getCourseQuestion(contentId);
    }

    @PostMapping("/insert")
    @Permission
    public Object insertQuestion(@RequestBody CourseQuestion courseQuestion){
        return courseQuestionService.insert(courseQuestion);
    }

    @PostMapping("/delete")
    @Permission
    public Object deleteQuestion(@RequestParam int id){
        return courseQuestionService.delete(id);
    }

    @PostMapping("/update")
    @Permission
    public Object updateQuestion(@RequestBody CourseQuestion courseQuestion){
        return courseQuestionService.update(courseQuestion);
    }

    @PostMapping("/submit")
    public Object submit(@RequestParam List<Boolean> inputs, @RequestParam int contentId, HttpServletRequest request){
        return courseQuestionService.submit(inputs,contentId, UserUtils.getUserId(request,jwtConfig));
    }

    @GetMapping("/check/finish")
    public Object checkFinished(@RequestParam int contentId, HttpServletRequest request){
        return courseQuestionService.checkFinished(contentId,UserUtils.getUserId(request,jwtConfig));
    }




}
