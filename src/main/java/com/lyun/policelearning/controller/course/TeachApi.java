package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.CourseService;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.TeachService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import javafx.beans.binding.ObjectExpression;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teach")
public class TeachApi {
    @Autowired
    TeachService teachService;
    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    RoleService roleService;
    @RequestMapping("/upload/picture")
    public Object uploadPicture(@RequestParam MultipartFile file){
        String fileName ="";
        if(!file.isEmpty()){
            //返回的是字节长度,1M=1024k=1048576字节 也就是if(fileSize<5*1048576)
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(StringUtils.isBlank(suffix)){
                return new ResultBody<>(false,500,"图片过大");
            }

            fileName = System.currentTimeMillis()+suffix;
            if(!new File(PathTools.getRunPath()+"/upload").exists()){
                new File(PathTools.getRunPath()+"/upload").mkdir();
            }
            String saveFileName = PathTools.getRunPath() +"/upload/coursePicture/"+fileName;
            File dest = new File(saveFileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultBody<>(false,500,"未保存成功");
            }
        }else {
            return new ResultBody<>(false,500,"其他错误");
        }
        InetAddress address = null;
        String imgUrl = "http://" + address.getHostAddress() + ":8080/api/upload/coursePicture/" + fileName;
        List<JSONObject> res = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",imgUrl);
        res.add(jsonObject);
        return new ResultBody<>(true,200,res);
    }

    @RequestMapping("/upload/video")
    @ResponseBody
    public Object editorOfVideo(@RequestParam("file") MultipartFile file) {
        String fileName ="";
        if(!file.isEmpty()){
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(StringUtils.isBlank(suffix)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message","文件没有后缀名，请重新上传");
                return new ResultBody<>(false,500,jsonObject);
            }

            fileName = System.currentTimeMillis()+suffix;
            if(!new File(PathTools.getRunPath()+"/upload").exists()){
                new File(PathTools.getRunPath()+"/upload").mkdir();
            }
            String saveFileName = PathTools.getRunPath() +"/upload/courseVideo/"+fileName;
            File dest = new File(saveFileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message",e.getMessage());
                return new ResultBody<>(false,500,jsonObject);
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","上传出错");
            return new ResultBody<>(false,500,jsonObject);
        }
        InetAddress address = null;
        String videoUrl = "http://" + address.getHostAddress() + ":8080/api/upload/courseVideo/" + fileName;
        List<JSONObject> res = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",videoUrl);
        res.add(jsonObject);
        return new ResultBody<>(true,200,res);
    }

    /**
     * 根据id返回课程内容
     * @param id
     * @return
     */
    @RequestMapping(value = "getById",method = RequestMethod.GET)
    public Object getById(@RequestParam int id){
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        //JSONObject res = new JSONObject();
        return new ResultBody<>(true,200,teachService.getById(id));
    }

    /**
     * 保存课程的内容
     * @param content
     * @return 返回课程的id
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Object save(@RequestParam String content){
        if(content.isEmpty()){
            return new ResultBody<>(false,500,"content is empty");
        }
        int id = teachService.save(content);
        return new ResultBody<>(true,200,id);
    }

    /**
     * 修改课程的内容
     * 传入id 然后也要返回id
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object update(@RequestParam int id,@RequestParam String content){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        teachService.update(id, content);
        return new ResultBody<>(true,200,id);
    }



    /**
     * 上传或修改课程信息（课程名称、课时名称、内容id）
     * @param id
     * @param courseName
     * @param name
     * @param request
     * @return
     */
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @SneakyThrows
    public Object addVideo(@RequestParam("id") int id,
                           @RequestParam("courseName") String courseName,
                           @RequestParam("name") String name, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1, jwtConfig,userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        if (id <= 0){
            return new ResultBody<>(false,501,"error id");
        }
        if (courseName == null || name == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (courseService.getCourseByName(courseName) == null){
            return new ResultBody<>(false,501,"course not found");
        }
        JSONObject course = courseService.getCourseByName(courseName);
        JSONArray catalogue = course.getJSONArray("catalogue");
        if (catalogue == null){
            catalogue = new JSONArray();
        }
        boolean find = false;
        for (Object o : catalogue) {
            JSONObject json = (JSONObject) o;
            if (json.getString("name").equals(name)){
                json.put("id",id);
                find = true;
            }
        }
        if (!find){
            JSONObject tmp = new JSONObject();
            tmp.put("name",name);
            tmp.put("id",id);
            catalogue.add(tmp);
        }
        //改变catalogue
        courseService.changeCatalogue(courseName,catalogue);
        return new ResultBody<>(true,200,null);
    }
}
