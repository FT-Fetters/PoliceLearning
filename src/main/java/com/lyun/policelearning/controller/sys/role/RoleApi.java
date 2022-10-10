package com.lyun.policelearning.controller.sys.role;

import com.lyun.policelearning.entity.Role;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleApi {

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/select",method = RequestMethod.GET)
    public Object getAll(@RequestParam int pageNum,@RequestParam int pageSize){
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        return new ResultBody<>(true,200,PageUtil.getPage(pageRequest,roleService.findAll()));
    }
}
