package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.*;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 后台管理课程接口
 */
@RestController
@RequestMapping("/course/manage")
public class CourseManageApi {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    TeachService teachService;

    @Autowired
    CourseContentService courseContentService;

    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    RoleService roleService;

    @Permission(admin = true)
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

    @Permission(admin = true)
    @RequestMapping(value = "/get/type",method = RequestMethod.GET)
    public Object getByType(@RequestParam String type){
        return new ResultBody<>(true,200,courseService.getByType(type));
    }

    @Permission(admin = true)
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
    @Permission(admin = true)
    @RequestMapping("/publish")
    public Object publishCourse(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String name = data.getString("name");
        String introduce = data.getString("introduce");
        String type = data.getString("type");
        Long planTime = data.getLong("plan_time");
        if (name == null || introduce == null || type == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (courseService.publish(name,introduce,type)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"unknown error, maybe name is exist");
        }
    }

    @Permission(admin = true)
    @SneakyThrows
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Object addVideo(@RequestParam("file") MultipartFile file,
                           @RequestParam("courseName") String courseName,
                           @RequestParam("name") String name, HttpServletRequest request){
        if (file.isEmpty()){
            return new ResultBody<>(false,501,"file is empty");
        }
        if (courseName == null || name == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (courseService.getCourseByName(courseName) == null){
            return new ResultBody<>(false,501,"course not found");
        }
        String path = PathTools.getRunPath() +
                "/video/" +
                DigestUtils.md5DigestAsHex((courseName+name).getBytes(StandardCharsets.UTF_8)) +
                ".mp4";
        File filePath = new File(path);
        file.transferTo(filePath);
        JSONObject course = courseService.getCourseByName(courseName);
        JSONArray catalogue = course.getJSONArray("catalogue");
        if (catalogue == null){
            catalogue = new JSONArray();
        }
        boolean find = false;
        for (Object o : catalogue) {
            JSONObject json = (JSONObject) o;
            if (json.getString("name").equals(name)){
                json.put("id",DigestUtils.md5DigestAsHex((courseName+name).getBytes(StandardCharsets.UTF_8)));
                find = true;
            }
        }
        if (!find){
            JSONObject tmp = new JSONObject();
            tmp.put("name",name);
            tmp.put("id",DigestUtils.md5DigestAsHex((courseName+name).getBytes(StandardCharsets.UTF_8)));
            catalogue.add(tmp);
        }
        //改变catalogue
        courseService.changeCatalogue(courseName,catalogue);
        return new ResultBody<>(true,200,null);
    }

    @Permission(admin = true)
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(HttpServletResponse response, HttpServletRequest request){
        return new ResultBody<>(true,200,courseService.count());
    }

    /**
     * 删除课程并且将对应的小结也一起删掉
     * @param id
     * @return
     */
    @Permission(admin = true)
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        JSONObject course = courseService.getCourseById(id);
        JSONArray catalogue = course.getJSONArray("catalogue");
        if(catalogue != null){
            for (Object o : catalogue){
                JSONObject json = (JSONObject) o;
                int tid = json.getInteger("id");
                teachService.delete(tid);
            }
        }
        courseService.delete(id);
        return new ResultBody<>(true,200,null);
    }

    @Permission
    @GetMapping("/get/finish/state")
    public Object getCourseFinishState(long courseId){
        return courseContentService.getCourseFinishState(courseId);
    }
}
