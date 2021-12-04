package com.lyun.policelearning.controller.comment;


import com.lyun.policelearning.service.CommentService;
import com.lyun.policelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comment/manage")
@RestController
public class CommentManageApi {
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;


    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteComment(){
        return null;

    }
}
