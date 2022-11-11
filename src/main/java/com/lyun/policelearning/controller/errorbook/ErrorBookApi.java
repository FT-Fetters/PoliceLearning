package com.lyun.policelearning.controller.errorbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.ErrorBookService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/errorBook")
public class ErrorBookApi {
    @Autowired
    ErrorBookService errorBookService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    /**
     * 将错题加入至错题集
     * @param data
     * @param request
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addQuestion(@RequestBody JSONObject data, HttpServletRequest request){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
        //type: 1=判断题  2=单选题  3=多选题
        Integer type = data.getInteger("type");
        Integer questionId = data.getInteger("questionId");
        if (type == null || questionId == null){
            return new ResultBody<>(false,501,"missing parameter");
        }
        if (type < 1 || type > 3){
            return new ResultBody<>(false,502,"error type");
        }
        if(errorBookService.save(userId,type,questionId)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"exist");
        }
    }

    /**
     * 一次性传入多个错题
     */
    @RequestMapping(value = "/add/some",method = RequestMethod.POST)
    public Object addSome(@RequestBody JSONArray array,HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        for(Object o : array){
            LinkedHashMap hashMap = (LinkedHashMap) o;
            Integer type = (Integer) hashMap.get("type");
            Integer questionId = (Integer) hashMap.get("questionId");
            if (type < 1 || type > 3){
                return new ResultBody<>(false,502,"error type");
            }
            errorBookService.save(userId,type,questionId);
        }
        return new ResultBody<>(true,200,null);
    }

    /**
     * 查看错题集
     * @param request
     * @return 返回题目、选项、答案
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object findAll(HttpServletRequest request){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
        JSONObject res = errorBookService.findAll(userId);
        return new ResultBody<>(true,200,res);
    }
    @RequestMapping(value = "/all/delete",method = RequestMethod.GET)
    public Object delete(HttpServletRequest request, @RequestBody JSONObject data){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
        Integer type = data.getInteger("type");
        Integer questionId = data.getInteger("questionId");
        errorBookService.delete(userId,type,questionId);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int type,@RequestParam int id,HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        errorBookService.delete(userId,type,id);
        return new ResultBody<>(true,200,null);
    }
}
