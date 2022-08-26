package com.lyun.policelearning.controller.sys.role;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Permission
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
        String name = data.getString("name");
        if (name == null)return new ResultBody<>(false,500,"miss parameter");
        roleService.newRole(name,false);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteRole(@RequestBody JSONObject data,HttpServletRequest request){
        Integer id = data.getInteger("id");
        roleService.deleteRole(id);
        return new ResultBody<>(true,200,null);
    }

    @PostMapping("/update")
    public Object update(@RequestBody JSONObject data){
        if (data.getString("name")==null || data.getInteger("id") ==null)
            return new ResultBody<>(true,-1,"miss parameter");
        roleService.updateRole(data.getString("name"),roleService.findById(data.getInteger("id")).isAdmin(),data.getInteger("id"));
        return new ResultBody<>(true,200,null);
    }

    @PostMapping("/update/admin")
    public Object setAdmin(@RequestBody JSONObject data){
        int role_id = data.getInteger("role_id");
        boolean admin = data.getBoolean("admin");
        roleService.updateAdmin(role_id,admin);
        return new ResultBody<>(true,200,null);
    }

    @GetMapping("/dictionary")
    public Object getRoleDictionary(){
        return new ResultBody<>(true,200,roleService.roleDictionary());
    }


}
