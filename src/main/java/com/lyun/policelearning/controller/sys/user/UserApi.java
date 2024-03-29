package com.lyun.policelearning.controller.sys.user;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.SysLogAnnotation;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
//    public Object login(@RequestBody JSONObject data  , HttpServletRequest request,HttpServletResponse response){
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
    @SysLogAnnotation(opModel = "登录模块", opType = "登录",opDesc = "用户登录")
    public Object login(@RequestParam String username,
                        @RequestParam String password,HttpServletRequest request){
        JSONObject res = new JSONObject();
        if (userService.check(username,password)){
            User user = userService.getByUsername(username);
            String userId = user.getId() + "";
            String token = jwtConfig.createToken(userId);
            String nickName = user.getNickname();
            boolean isAdmin = userService.isAdmin(user.getId());
            if (!token.equals("")){
                res.put("token",token);
                res.put("userName",username);
                res.put("nickName",nickName);
                res.put("isAdmin",isAdmin);
            }
            return new ResultBody<>(true,200,res);

        }else {
            return new ResultBody<>(false,500,"error password");
        }
    }

    @RequestMapping(value = "/login/pki")
    @SysLogAnnotation(opModel = "登录模块", opType = "登录",opDesc = "用户登录")
    public Object pkiLogin(HttpServletRequest request, HttpServletResponse response){
        //id_token,code是单点登录之后跳转到指定地址携带回来的参数
        String idToken = request.getParameter("id_token");
        String code = request.getParameter("code");
        if (idToken == null || code == null || idToken.equals("") || code.equals("")){
            return new ResultBody<>(false,500,"miss parameter");
        }
        //解析当前认证信息
        String userAuthJsonStr=JwtUtil.getUserInfo(idToken);
        //System.out.println(userAuthJsonStr);
        JSONObject userJson = JSONObject.parseObject(userAuthJsonStr);
        String name = userJson.getString("name");
        String username = userJson.getString("username");
        if (userService.getByUsername(username) == null){
            userService.newUser(username,"123456",name,name,2,"","男",null);
        }
        String userId = userService.getByUsername(username).getId() + "";
        String token = jwtConfig.createToken(userId);
        String nickName = userService.getByUsername(username).getNickname();
        JSONObject res = new JSONObject();
        if (!token.equals("")){
            res.put("token",token);
            res.put("userName",username);
            res.put("nickName",nickName);
        }
        return new ResultBody<>(true,200,res);
    }

    @RequestMapping("/login/app")
    @SysLogAnnotation(opModel = "登录模块", opType = "登录",opDesc = "用户登录")
    public Object appLogin(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response){
        String username = data.getString("username");
        String nickname = data.getString("nickname");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        if (username == null || nickname == null || phone == null || sex == null)
            return new ResultBody<>(false,500,"miss parameter");
        JSONObject res = new JSONObject();
        if (userService.getByUsername(username) == null){
            userService.newUser(username,"123456",nickname,nickname,2,phone,sex,null);
            res.put("first_login",true);
        }
        String userId = String.valueOf(userService.getByUsername(username).getId());
        String token = jwtConfig.createToken(userId);
        String nickName = userService.getByUsername(username).getNickname();
        if (!token.equals("")){
            res.put("token",token);
            res.put("userName",username);
            res.put("nickName",nickName);
            res.put("first_login",false);
        }
        return new ResultBody<>(true,200,res);
    }

    @Data
    public static class RegInf{
        private String name;
        private String phone;
        private String nickname;
        private String dept;
        private String sex;
        private String username;
    }

    @PostMapping("/first/login/reg/inf")
    public Object regInf(@RequestBody RegInf body){
        return userService.regInf(body);
    }



    /**
     * 用户获取头像
     */
    @SneakyThrows
    @RequestMapping(value = "/headPortrait/get",method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "登录模块", opType = "查询",opDesc = "用户获取头像")
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
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                boolean mkdirs = saveDir.mkdirs();
            }
            File saveFile = new File(savePath + userId + ".jpg");
            boolean b = saveFile.setWritable(true, false);
            file.transferTo(saveFile);
            LogUtils.log(username+" upload head portrait","upload",true,request);
            return new ResultBody<>(true,200,null);
        }else {
            LogUtils.log(username + " upload head portrait fail, cause error suffix","upload",true,request);
            return new ResultBody<>(false,501,"wrong file type");
        }
    }

}
