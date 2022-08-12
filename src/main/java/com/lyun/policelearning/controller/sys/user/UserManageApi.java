package com.lyun.policelearning.controller.sys.user;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.POJONode;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.PinYinUtil;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Permission
@RestController
@RequestMapping("/user/manage")
public class UserManageApi {

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;

    @Permission(admin = true)
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,userService.count());
    }

    @Permission(admin = true)
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newUser(@RequestBody JSONObject data){
        String username = data.getString("username");
        String password = data.getString("password");
        String nickname = data.getString("nickname");
        String realname = data.getString("realname");
        Integer role = data.getInteger("role");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        Long dept = data.getLong("dept");
        if (username == null || password == null || role == null || sex == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        if (userService.getByUsername(username) == null){
            userService.newUser(username,password,nickname,realname,role,phone,sex,dept);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"username is exists");
        }
    }

    @Permission(admin = true)
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteUser(@RequestBody JSONObject data, HttpServletRequest request){
        Integer id = data.getInteger("id");
        if (id == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        userService.deleteUser(id);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateUser(@RequestBody JSONObject data){
        Integer id = data.getInteger("id");
        String username = data.getString("username");
        String nickname = data.getString("nickname");
        String realname = data.getString("realname");
        Integer role = data.getInteger("role");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        Long dept = data.getLong("dept");
        if (username == null || role == null || sex == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        userService.updateUser(id,username,nickname,realname,role,phone,sex,dept);
        return new ResultBody<>(true,200,null);
    }

    @Permission(admin = true)
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object getAll(){
        return new ResultBody<>(true,200,userService.findAll());
    }

    @RequestMapping(value = "/update/nickname",method = RequestMethod.POST)
    public Object updateNickname(@RequestBody JSONObject data,HttpServletRequest request){
        if (!UserUtils.getUsername(request, jwtConfig).equals(data.getString("username"))){
            return new ResultBody<>(false,-1,"not allow");
        }
        userService.changeNickname(data.getString("username"),data.getString("nickname"));
        return new ResultBody<>(true,200,null);

    }


    @RequestMapping(value = "/change/password",method = RequestMethod.POST)
    public Object changePassword(@RequestBody JSONObject data){
        String username = data.getString("username");
        String password = data.getString("password");
        if (username == null || password == null)return new ResultBody<>(false,500,"miss parameter");
        if (userService.getByUsername(username)==null)return new ResultBody<>(false,501,"unknown username");
        userService.changePassword(username,password);
        return new ResultBody<>(true,200,null);
    }

    @Permission(admin = true)
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public Object importCop(@RequestBody JSONObject data){
        String name = data.getString("name");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        Long dept = data.getLong("dept");
        if (name == null || phone == null || sex == null)return new ResultBody<>(false,500,"miss parameter");
        if (name.length() > 4)return new ResultBody<>(false,501,"name is too long");
        String username = PinYinUtil.getPinyin(name) + new Random().nextInt(3000);
        String password = DigestUtils.md5DigestAsHex((username + phone + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8)).substring(0,10);
        userService.newUser(username,password,name,name,2,phone,sex,dept);
        JSONObject ret = new JSONObject();
        ret.put("username",username);
        ret.put("password",password);
        return new ResultBody<>(true,200,ret);
    }

}
