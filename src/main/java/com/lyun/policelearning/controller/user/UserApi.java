package com.lyun.policelearning.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.*;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    UserService userService;

    /**
     * 用户登录接口
     * @param data 含参username,password
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object login(@RequestBody JSONObject data, HttpServletRequest request,HttpServletResponse response){
        String username = data.getString("username");
        String password = data.getString("password");
        if (username != null && password != null && userService.check(username,password)){
            String token = User.Token.createToken(username);
            CookieUtils.writeCookie(response,"token",token,3600);
            LogUtils.log(username + " login","login",true,request);
            return new ResultBody<>(true,200,null);
        }else {
            LogUtils.log("login fail","login",true,request);
            return new ResultBody<>(false,501,"error password or username is not exist");
        }
    }


    /**
     *登出
     */
    @RequestMapping("/logout")
    public Object logout(HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId != -1){
            String username = UserUtils.getUsername(request);
            String token = CookieUtils.getCookie(request,"token");
            User.Token.dropToken(username,token);
            LogUtils.log(username +" logout","logout",true,request);
            return new ResultBody<>(true,200,null);
        }else {
            LogUtils.log("try to logout, but not login","logout",true,request);
            return new ResultBody<>(false,500,"not login");
        }
    }


    /**
     * 用户获取头像
     */
    @SneakyThrows
    @RequestMapping(value = "/headPortrait/get",method = RequestMethod.GET)
    public Object getHeadPortrait(HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId != -1){
            String username = UserUtils.getUsername(request);
            String headPath = PathTools.getRunPath() + "/head/";
            if (!new File(headPath).exists())new File(headPath).mkdirs();
            File userHeadPortrait = new File(headPath + userId+".jpg");
            if (userHeadPortrait.exists()){
                BufferedImage bufferedImage = ImageIO.read(userHeadPortrait);
                String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                LogUtils.log(username+ " get head portrait","get",true,request);
                return new ResultBody<>(true,200,imgBase64);
            }else {
                LogUtils.log(username+" try to get head portrait but head portrait file not exist","get",false,request);
                return new ResultBody<>(false,501,"head portrait not exists");
            }
        }else {
            LogUtils.log("try to get head portrait but not login","get",true,request);
            return new ResultBody<>(false,500,"not login");
        }
    }


    /**
     * 上传头像
     * @param file 头像文件
     * @param request request
     */
    @SneakyThrows
    @RequestMapping(value = "/headPortrait/upload",method = RequestMethod.POST)
    public Object uploadHeadPortrait(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId != -1){
            String fileName = file.getOriginalFilename();
            String username = UserUtils.getUsername(request);
            assert fileName != null;
            int unixSep = fileName.lastIndexOf('/');
            int winSep = fileName.lastIndexOf('\\');
            int pos = (Math.max(winSep, unixSep));
            if (pos != -1)  {
                fileName = fileName.substring(pos + 1);
            }
            String[] filenameSplit = fileName.split("\\.");
            String suffix = filenameSplit[filenameSplit.length-1];
            if (suffix.equals("jpg")){
                String savePath = PathTools.getRunPath() + "/head/";
                File saveFile = new File(savePath + userId + ".jpg");
                if (saveFile.setWritable(true, false)){
                    OutputStream outputStream = new FileOutputStream(saveFile);
                    IOUtils.copy(file.getInputStream(),outputStream);
                    LogUtils.log(username+" upload head portrait","upload",true,request);
                    return new ResultBody<>(true,200,null);
                }else {
                    LogUtils.log(username + " upload head portrait fail, unknown error","upload",true,request);
                    return new ResultBody<>(false,502,"unknown error");
                }
            }else {
                LogUtils.log(username + " upload head portrait fail, cause error suffix","upload",true,request);
                return new ResultBody<>(false,501,"wrong file type");
            }
        }else {
            LogUtils.log("not login","upload",false,request);
            return new ResultBody<>(false,500,"not login");
        }
    }
}
