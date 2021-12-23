package com.lyun.policelearning.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.*;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

//    /**
//     * 用户登录接口
//     * @param data 含参username,password
//     */
//    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    public Object login(@RequestBody JSONObject data, HttpServletRequest request,HttpServletResponse response){
//        String username = data.getString("username");
//        String password = data.getString("password");
//        if (username != null && password != null && userService.check(username,password)){
//            String token = User.Token.createToken(username);
//            CookieUtils.writeCookie(response,"token",token,3600);
//            LogUtils.log(username + " login","login",true,request);
//            return new ResultBody<>(true,200,null);
//        }else {
//            LogUtils.log("login fail","login",true,request);
//            return new ResultBody<>(false,501,"error password or username is not exist");
//        }
//    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object login(@RequestParam String username,
                        @RequestParam String password){
        JSONObject res = new JSONObject();
        if (userService.check(username,password)){
            String userId = userService.getByUsername(username).getId() + "";
            String token = jwtConfig.createToken(userId);
            String nickName = userService.getByUsername(username).getNickname();
            if (!token.equals("")){
                res.put("token",token);
                res.put("userName",username);
                res.put("nickName",nickName);
            }
            return new ResultBody<>(true,200,res);

        }else {
            return new ResultBody<>(false,500,"error password");
        }
    }



    /**
     * 用户获取头像
     */
    @SneakyThrows
    @RequestMapping(value = "/headPortrait/get",method = RequestMethod.GET)
    public Object getHeadPortrait(HttpServletRequest request){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
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
    }


    /**
     * 上传头像
     * @param file 头像文件
     * @param request request
     */
    @SneakyThrows
    @RequestMapping(value = "/headPortrait/upload",method = RequestMethod.POST)
    public Object uploadHeadPortrait(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
        String fileName = file.getOriginalFilename();
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
    }

}
