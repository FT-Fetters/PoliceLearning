package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseApi {

    @Autowired
    CourseService courseService;

    /**
     * 获取所有的课程
     * @return 所有的课程列表
     */
    @RequestMapping("/all")
    public Object getAll(){
        return new ResultBody<>(true,200,courseService.findAll());
    }

    /**
     * 通过id获取指定的课程
     * @param id 要获取的课程的id
     * @return 指定的课程
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object get(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        JSONObject res = courseService.getCourseById(id);
        if (res != null){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"unknown id");
        }
    }

    /**
     * 获取指定id的课程下的目录json列表
     * @param id 指定的id
     * @return 指定id的课程的目录
     */
    @RequestMapping(value = "/catalogue",method = RequestMethod.GET)
    public Object getCourseCatalogue(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        JSONArray res = courseService.getCatalogue(id);
        if (res != null){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"not found this id");
        }
    }

    @RequestMapping(value = "/introduce",method = RequestMethod.GET)
    public Object getCourseIntroduce(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        String res = courseService.getIntroduce(id);
        if (res != null){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"not found this id");
        }
    }
}
