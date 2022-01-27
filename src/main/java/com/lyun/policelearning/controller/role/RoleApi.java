package com.lyun.policelearning.controller.role;

import com.lyun.policelearning.entity.Role;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleApi {

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object getAll(){
        return new ResultBody<>(true,200,roleService.findAll());
    }
}
