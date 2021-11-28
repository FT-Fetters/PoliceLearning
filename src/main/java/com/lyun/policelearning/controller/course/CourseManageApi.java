package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;

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

    /**
     * 发布课程
     * @param data
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/publish")
    public Object publishCourse(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String name = data.getString("name");
        String introduce = data.getString("introduce");
        String type = data.getString("type");
        if (name == null || introduce == null || type == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (courseService.publish(name,introduce,type)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"unknown error, maybe name is exist");
        }
    }

    @SneakyThrows
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Object addVideo(@RequestParam("file") MultipartFile file,@RequestParam("courseName") String courseName,@RequestParam("videoName") String videoName){
        if (!file.isEmpty()){
            return new ResultBody<>(false,501,"file is empty");
        }
        if (courseName == null || videoName == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (courseService.getCourseByName(courseName) == null){
            return new ResultBody<>(false,501,"course not found");
        }
        String path = PathTools.getRunPath() +
                "/video" +
                DigestUtils.md5DigestAsHex((courseName+videoName).getBytes(StandardCharsets.UTF_8)) +
                ".mp4";
        File filePath = new File(path);
        file.transferTo(filePath);
        JSONObject course = courseService.getCourseByName(courseName);
        JSONArray catalogue = course.getJSONArray("catalogue");
        JSONObject tmp = new JSONObject();
        tmp.put("name",courseName);
        tmp.put("id",DigestUtils.md5DigestAsHex((courseName+videoName).getBytes(StandardCharsets.UTF_8)));
        catalogue.add(tmp);
        //改变catalogue
        return new ResultBody<>(true,200,null);
    }


}
