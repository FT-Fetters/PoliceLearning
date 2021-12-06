package com.lyun.policelearning.controller.comment;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.CommentService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@RestController
@RequestMapping("/comment")
public class CommentApi {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;


    /**
     * 获取指定id资讯的评论
     * @param id 资讯id
     * @return 评论
     */
    @RequestMapping("/get/inf")
    public Object getInfComment(@RequestParam int id){
        return new ResultBody<>(true,200,commentService.getInfComment(id));
    }

    /**
     * 获取指定id新规的评论
     * @param id 新规id
     * @return 评论
     */
    @RequestMapping("/get/rule")
    public Object getRuleComment(@RequestParam int id){
        return new ResultBody<>(true,200,commentService.getRuleComment(id));
    }


    /**
     * 评论资讯
     * @param data 传入参数
     * @param response response
     * @param request request
     * @return result
     */
    @RequestMapping(value = "/inf",method = RequestMethod.POST)
    public Object commentInf(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId != -1){
            String username = userService.getById(userId).getUsername();
            Integer parentId = data.getInteger("parentId");
            Date date = new Date(System.currentTimeMillis());
            String content = data.getString("content");
            Integer hostId = data.getInteger("hostId");
            if (content == null || hostId == null){
                LogUtils.log(username+" comment but missing parameter","fail",false,request);
                return new ResultBody<>(false,501,"missing parameter");
            }
            if (parentId == null){
                commentService.comment(userId,date,content,"inf",hostId);
                LogUtils.log(username+" comment inf,content is:" + content,"info",false,request);
            }else {
                commentService.comment(userId,parentId,date,content,"inf",hostId);
                LogUtils.log(username+" comment secondComment in inf,content is:" + content,"info",false,request);
            }
            return new ResultBody<>(true,200,null);
        }else {
            LogUtils.log("comment information but not login","comment",true,request);
            return new ResultBody<>(false,500,"not login");
        }
    }


    @RequestMapping(value = "/rule",method = RequestMethod.POST)
    public Object commentRule(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if (userId != -1){
            String username = userService.getById(userId).getUsername();
            Integer parentId = data.getInteger("parentId");
            Date date = new Date(System.currentTimeMillis());
            String content = data.getString("content");
            Integer hostId = data.getInteger("hostId");
            if (content == null || hostId == null){
                LogUtils.log(username+" comment but missing parameter","fail",false,request);
                return new ResultBody<>(false,501,"missing parameter");
            }
            if (parentId == null){
                commentService.comment(userId,date,content,"rule",hostId);
                LogUtils.log(username+" comment rule,content is:" + content,"info",false,request);
            }else {
                commentService.comment(userId,parentId,date,content,"inf",hostId);
                LogUtils.log(username+" comment secondComment in rule,content is:" + content,"info",false,request);
            }
            return new ResultBody<>(true,200,null);
        }else {
            LogUtils.log("comment rule but not login","comment",true,request);
            return new ResultBody<>(false,500,"not login");
        }
    }

}
