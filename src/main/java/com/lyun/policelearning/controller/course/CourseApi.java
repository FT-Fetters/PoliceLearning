package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.SysLogAnnotation;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.handler.NonStaticResourceHttpRequestHandler;
import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.utils.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseApi {

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 获取所有的课程
     *
     * @return 所有的课程列表
     */
    @RequestMapping("/all")
    @SysLogAnnotation(opModel = "课程模块", opDesc = "用户获取所有课程", opType = "查询")
    public Object getAll(HttpServletRequest request) {
        int userId = UserUtils.getUserId(request, jwtConfig);
        return new ResultBody<>(true, 200, courseService.findAll(userId));
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public Object search(@RequestParam String word){
        return new ResultBody<>(true, 200, courseService.search(word));
    }

    /**
     * 通过id获取指定的课程
     *
     * @param id 要获取的课程的id
     * @return 指定的课程
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "课程模块", opDesc = "用户获取指定课程", opType = "查询")
    public Object get(@RequestParam int id, HttpServletRequest request) {
        if (id <= 0) {
            return new ResultBody<>(false, 500, "error id");
        }
        LogUtils.log("get course list", "get", true, request);
        JSONObject res = courseService.getCourseById(id);
        if (res != null) {
            return new ResultBody<>(true, 200, res);
        } else {
            return new ResultBody<>(false, 501, "unknown id");
        }
    }

    /**
     * 获取指定id的课程下的目录json列表
     *
     * @param id 指定的id
     * @return 指定id的课程的目录
     */
    @RequestMapping(value = "/catalogue", method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "课程模块", opDesc = "用户获取指定课程目录", opType = "查询")
    public Object getCourseCatalogue(@RequestParam int id, HttpServletRequest request) {
        if (id <= 0) {
            return new ResultBody<>(false, 500, "error id");
        }
        LogUtils.log("get course catalogue", "get", true, request);
        JSONArray res = courseService.getCatalogue(id);
        if (res != null) {
            return new ResultBody<>(true, 200, res);
        } else {
            return new ResultBody<>(false, 501, "not found this id");
        }
    }

    @RequestMapping(value = "/introduce", method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "课程模块", opDesc = "用户获取课程介绍", opType = "查询")
    public Object getCourseIntroduce(@RequestParam int id) {
        if (id <= 0) {
            return new ResultBody<>(false, 500, "error id");
        }
        String res = courseService.getIntroduce(id);
        return new ResultBody<>(true, 200, res);
    }

//    public Object




    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @SneakyThrows
    @RequestMapping(value = "/video",method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "课程模块", opDesc = "用户获取课程视频", opType = "查询")
    public void playVideo(@RequestParam String id, HttpServletRequest request, HttpServletResponse response){
        String savePath = PathTools.getRunPath()+"/video/";
        if(!new File(savePath).exists()){
            new File(savePath).mkdirs();
        }
        String realPath = PathTools.getRunPath() + "/video/"+id+".mp4";
        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)){
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)){
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE,filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request,response);
        }else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }
}
