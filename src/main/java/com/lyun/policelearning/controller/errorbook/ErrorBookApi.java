package com.lyun.policelearning.controller.errorbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.ErrorBookService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/errorBook")
public class ErrorBookApi {
    @Autowired
    ErrorBookService errorBookService;

    @Autowired
    UserService userService;

    /**
     * 将错题加入至错题集
     * @param data
     * @param request
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public Object addQuestion(@RequestBody JSONObject data, HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if(userId != -1){
            //type: 1=判断题  2=单选题  3=多选题
            Integer type = data.getInteger("type");
            Integer questionId = data.getInteger("questionId");
            if (type == null || questionId == null){
                return new ResultBody<>(false,501,"missing parameter");
            }
            if (type < 1 || type > 3){
                return new ResultBody<>(false,502,"error type");
            }
            errorBookService.save(userId,type,questionId);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"no login");
        }
    }

    /**
     * 查看错题集
     * @param request
     * @return 返回题目、选项、答案
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object findAll(HttpServletRequest request){
        int userId = UserUtils.isLogin(request,userService);
        if(userId != -1){
            List<JSONObject> res = errorBookService.findAll(userId);
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,500,"no login");
        }
    }
    @RequestMapping(value = "/all/delete",method = RequestMethod.GET)
    public Object delete(HttpServletRequest request, @RequestBody JSONObject data){
        int userId = UserUtils.isLogin(request,userService);
        Integer type = data.getInteger("type");
        Integer questionId = data.getInteger("questionId");
        if(userId != -1){
            errorBookService.delete(userId,type,questionId);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"no login");
        }
    }
}
