package com.lyun.policelearning.controller.comment;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.CommentService;
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

@RequestMapping("/comment/manage")
@RestController
public class CommentManageApi {
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;


    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteComment(@RequestBody JSONObject data, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 1,jwtConfig, userService,roleService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        Integer id = data.getInteger("id");
        if (id==null)return new ResultBody<>(false,500,"miss parameter");
        commentService.deleteComment(id);

        return new ResultBody<>(true,200,null);

    }
}
