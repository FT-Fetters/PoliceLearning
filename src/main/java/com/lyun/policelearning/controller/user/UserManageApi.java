package com.lyun.policelearning.controller.user;

import com.lyun.policelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/manage")
public class UserManageApi {

    @Autowired
    UserService userService;
}
