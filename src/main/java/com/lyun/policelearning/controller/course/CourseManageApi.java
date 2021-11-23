package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理课程接口
 */
@RestController
@RequestMapping("/course/manage")
public class CourseManageApi {

    @Autowired
    CourseService courseService;

    @RequestMapping(value = "/change/type",method = RequestMethod.POST)
    public Object changeType(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        Integer id = data.getInteger("id");
        String type = data.getString("type");
        if(id == null || type == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if (courseService.changeType(id,type)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"unknown error, maybe is id not exist");
        }
    }

    @RequestMapping(value = "/get/type",method = RequestMethod.GET)
    public Object getByType(@RequestParam String type){
        return new ResultBody<>(true,200,courseService.getByType(type));
    }

    @RequestMapping(value = "/change/introduce",method = RequestMethod.POST)
    public Object changeIntroduce(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        Integer id = data.getInteger("id");
        String introduce = data.getString("introduce");
        if(id == null || introduce == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if (courseService.changeIntroduce(id,introduce)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"unknown error, maybe is id not exist");
        }
    }

    //未完成
    @RequestMapping("/publish")
    public Object publishCourse(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        return null;
    }


}
