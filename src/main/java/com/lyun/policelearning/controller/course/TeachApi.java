package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.Teach;
import com.lyun.policelearning.service.*;
import com.lyun.policelearning.utils.Constant;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import com.lyun.policelearning.utils.page.PageRequest;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
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
    StateService stateService;

    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    RoleService roleService;
    //创建一个值用于记录将要被删除的元素
    public static List<JSONObject> list = new ArrayList<>() ;
    @SneakyThrows
    @Permission(admin = true)
    @RequestMapping("/upload/picture")
    public Object uploadPicture(@RequestParam MultipartFile file){
        String fileName ="";
        if(!file.isEmpty()){
            //返回的是字节长度,1M=1024k=1048576字节 也就是if(fileSize<5*1048576)
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(StringUtils.isBlank(suffix)){
                return new ErrorResponse(1,"图片过大");
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
                return new ErrorResponse(1,e.getMessage());
            }
        }else {
            return new ErrorResponse(1,"其它问题，请联系管理员");
        }
        InetAddress address = InetAddress.getLocalHost();
        String imgUrl = Constant.BASE_URL + "api/upload/coursePicture/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",imgUrl);
        return new PictureResponse(0,jsonObject);
    }

    @SneakyThrows
    @RequestMapping("/upload/video")
    @ResponseBody
    @Permission(admin = true)
    public Object editorOfVideo(@RequestParam("file") MultipartFile file) {
        String fileName ="";
        if(!file.isEmpty()){
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(StringUtils.isBlank(suffix)){
                return new ErrorResponse(1,"文件没有后缀名，请重新上传");
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
                return new ErrorResponse(1,e.getMessage());
            }
        }else {
            return new ErrorResponse(1,"上传出错");
        }
        InetAddress address = InetAddress.getLocalHost();;
        String url = address.getHostAddress();
        String videoUrl = Constant.BASE_URL + "api/upload/courseVideo/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",videoUrl);
        return new WangEditorResponse(0,jsonObject);
    }

    /**
     * 根据id返回课程内容
     * @param id
     * @return
     */
    @Permission(admin = false)
    @RequestMapping(value = "getById",method = RequestMethod.GET)
    public Object getById(@RequestParam int id,HttpServletRequest request){
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        //JSONObject res = new JSONObject();
        //更新课时状态表
        int userId = UserUtils.getUserId(request,jwtConfig);
        int tid = id;
        //如果不存在则更新
        if (!stateService.check(tid,userId)){
            stateService.insert(tid,userId);
        }
        return new ResultBody<>(true,200,teachService.getById(id));
    }
    /**
     * 2022.10.7
     * deng
     * 根据课时id分页返回已读人员列表
     */
    @Permission(admin = true)
    @RequestMapping(value = "/getState",method = RequestMethod.GET)
    public Object getState(/*@RequestParam Integer pageNum,@RequestParam Integer pageSize*/@RequestParam int tid){
        //PageRequest pageRequest = new PageRequest(pageNum,pageSize);
        return new ResultBody<>(true,200,stateService.getState(tid));
    }




    /**
     * 保存课程的内容
     * @return 返回课程的id
     */
    @Permission(admin = true)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Object save(@RequestBody Teach teach){
        /*if(content.isEmpty()){
            return new ResultBody<>(false,500,"content is empty");
        }*/
        int id = teachService.save(teach);
        return new ResultBody<>(true,200,id);
    }

    /**
     * 修改课程的内容
     * 传入id 然后也要返回id
     */
    @Permission(admin = true)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object update(@RequestBody Teach teach){
        int id = teach.getId();
        String content = teach.getContent();
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
    @Permission(admin = true)
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @SneakyThrows
    public Object addVideo(@RequestParam("id") int id,
                           @RequestParam("courseName") String courseName,
                           @RequestParam("name") String name, HttpServletRequest request){
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

    @Permission(admin = true)
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam String courseName,@RequestParam String name,HttpServletRequest request){
        if (courseName == null || name == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        JSONObject course = courseService.getCourseByName(courseName);
        List<JSONObject> catalogue = (List<JSONObject>) course.get("catalogue");
        for (JSONObject json : catalogue) {
            int i = 0;
            if (json.getString("name").equals(name)){
                 if(json.getInteger("id") != null){
                     teachService.delete(json.getInteger("id"));
                 }
                 list.add(json);
            }
            i++;
        }
        catalogue.removeAll(list);
        courseService.changeCatalogue(courseName,JSONArray.parseArray(catalogue.toString()));
        return new ResultBody<>(true,200,null);
    }
    @Data
    private class PictureResponse{
        int errno;
        Object data;
        public PictureResponse(int errno,Object data){
            this.errno=errno;
            this.data=data;
        }
        public PictureResponse(int errno){
            this.errno=errno;
        }
    }
    @Data
    private class ErrorResponse{
        int errno;
        Object message;
        public ErrorResponse(int errno,Object message){
            this.errno = errno;
            this.message = message;
        }
    }

    @Data
    private class WangEditorResponse{
        int errno;
        JSONObject data;
        public WangEditorResponse(int errno,JSONObject data){
            this.errno=errno;
            this.data=data;
        }
    }
}
