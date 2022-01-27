package com.lyun.policelearning.controller.role;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/role/manage")
public class RoleManageApi {
    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newRole(@RequestBody JSONObject data, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1, jwtConfig,userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        String name = data.getString("name");
        Integer power =data.getInteger("power");
        if (name == null || power == null)return new ResultBody<>(false,500,"miss parameter");
        roleService.newRole(name,power);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteRole(@RequestBody JSONObject data,HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1, jwtConfig,userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        Integer id = data.getInteger("id");
        roleService.deleteRole(id);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateRole(@RequestBody JSONObject data, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1, jwtConfig,userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        String name = data.getString("name");
        Integer power =data.getInteger("power");
        Integer id = data.getInteger("id");
        if (name == null || power == null)return new ResultBody<>(false,500,"miss parameter");
        roleService.updateRole(name,power,id);
        return new ResultBody<>(true,200,null);
    }


}
