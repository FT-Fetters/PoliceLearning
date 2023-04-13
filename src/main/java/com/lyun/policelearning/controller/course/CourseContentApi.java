package com.lyun.policelearning.controller.course;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.course.CourseContent;
import com.lyun.policelearning.service.*;
import com.lyun.policelearning.utils.Constant;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.UserUtils;
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
import java.util.Objects;

@RestController
@RequestMapping("/course/content")
public class CourseContentApi {

    @Autowired
    private CourseContentService courseContentService;

    @Autowired
    private CourseUsrLearnService courseUsrLearnService;


    @Autowired
    private JwtConfig jwtConfig;


    public static List<JSONObject> list = new ArrayList<>();

    @SneakyThrows
    @Permission()
    @RequestMapping("/upload/picture")
    public Object uploadPicture(@RequestParam MultipartFile file) {
        String fileName;
        if (!file.isEmpty()) {
            //返回的是字节长度,1M=1024k=1048576字节 也就是if(fileSize<5*1048576)
            String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            if (StringUtils.isBlank(suffix)) {
                return new ErrorResponse(1, "图片过大");
            }

            fileName = System.currentTimeMillis() + suffix;
            if (!new File(PathTools.getRunPath() + "/upload").exists()) {
                boolean mkdir = new File(PathTools.getRunPath() + "/upload").mkdir();
                if (!mkdir) return new ErrorResponse(1, "创建文件夹失败");
            }

            String saveFileName = PathTools.getRunPath() + "/upload/coursePicture/" + fileName;
            File dest = new File(saveFileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                boolean mkdir = dest.getParentFile().mkdir();
                if (!mkdir) return new ErrorResponse(1, "创建文件夹失败");
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new ErrorResponse(1, e.getMessage());
            }
        } else {
            return new ErrorResponse(1, "其它问题，请联系管理员");
        }
        InetAddress address = InetAddress.getLocalHost();
        String imgUrl = Constant.BASE_URL + "api/upload/coursePicture/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", imgUrl);
        return new PictureResponse(0, jsonObject);
    }

    @SneakyThrows
    @RequestMapping("/upload/video")
    @ResponseBody
    @Permission()
    public Object editorOfVideo(@RequestParam("file") MultipartFile file) {
        String fileName;
        if (!file.isEmpty()) {
            String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            if (StringUtils.isBlank(suffix)) {
                return new ErrorResponse(1, "文件没有后缀名，请重新上传");
            }

            fileName = System.currentTimeMillis() + suffix;
            if (!new File(PathTools.getRunPath() + "/upload").exists()) {
                boolean mkdir = new File(PathTools.getRunPath() + "/upload").mkdir();
                if (!mkdir) return new ErrorResponse(1, "创建文件夹失败");
            }
            String saveFileName = PathTools.getRunPath() + "/upload/courseVideo/" + fileName;
            File dest = new File(saveFileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                boolean mkdir = dest.getParentFile().mkdir();
                if (!mkdir) return new ErrorResponse(1, "创建文件夹失败");
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new ErrorResponse(1, e.getMessage());
            }
        } else {
            return new ErrorResponse(1, "上传出错");
        }
        InetAddress address = InetAddress.getLocalHost();
        ;
        String url = address.getHostAddress();
        String videoUrl = Constant.BASE_URL + "api/upload/courseVideo/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", videoUrl);
        return new WangEditorResponse(0, jsonObject);
    }

    @GetMapping("/get/id")
    public Object getById(@RequestParam int id, HttpServletRequest request) {
        return courseContentService.getById(id, UserUtils.getUserId(request, jwtConfig));
    }

    @Permission
    @RequestMapping("/get/state")
    public Object getState(@RequestParam int id) {
        return courseContentService.getState(id);
    }

    @Permission
    @PostMapping("/save")
    public Object save(@RequestBody CourseContent courseContent) {
        return courseContentService.save(courseContent);
    }

    @Permission
    @PostMapping("/update")
    public Object update(@RequestBody CourseContent courseContent) {
        return courseContentService.update(courseContent);
    }

    @Permission
    @PostMapping("/delete")
    public Object delete(@RequestParam int id) {
        return courseContentService.delete(id);
    }


    @Data
    static class submitTimeBody{
        private long learnTime;
        private long courseId;
        private long contentId;
    }

    @PostMapping("/submit/learn/time")
    public Object submitLearnTime(@RequestBody submitTimeBody body, HttpServletRequest request){
        return courseUsrLearnService.submitLearnTime(body.learnTime, body.courseId, body.contentId, UserUtils.getUserId(request, jwtConfig));
    }

    @Data
    static class CheckBody{
        private long courseId;
        private long contentId;
    }

    @PostMapping("/check/finished")
    public Object checkFinished(@RequestBody CheckBody body, HttpServletRequest request){
        return courseUsrLearnService.checkFinished(body.courseId, body.contentId, UserUtils.getUserId(request,jwtConfig));
    }
    @Data
    private static class PictureResponse {
        int errno;
        Object data;

        public PictureResponse(int errno, Object data) {
            this.errno = errno;
            this.data = data;
        }

        public PictureResponse(int errno) {
            this.errno = errno;
        }
    }

    @Data
    private static class ErrorResponse {
        int errno;
        Object message;

        public ErrorResponse(int errno, Object message) {
            this.errno = errno;
            this.message = message;
        }
    }

    @Data
    private static class WangEditorResponse {
        int errno;
        JSONObject data;

        public WangEditorResponse(int errno, JSONObject data) {
            this.errno = errno;
            this.data = data;
        }
    }

}
