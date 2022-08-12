package com.lyun.policelearning.controller.collect;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.CollectService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/collect")
@RestController
public class CollectApi {

    @Autowired
    CollectService collectService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Permission(admin = true)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object collect(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String username = UserUtils.getUsername(request,jwtConfig);
        int userId = UserUtils.getUserId(request,jwtConfig);
        Integer type = data.getInteger("type");
        Integer articleId = data.getInteger("articleId");
        if (type == null || articleId == null){
            LogUtils.log("collect but missing parameter","collect",true,request);
            return new ResultBody<>(false,501,"missing parameter");
        }
        if (type < 1 || type > 3){
            LogUtils.log(username+" collect but type is error","collect",false,request);
            return new ResultBody<>(false,502,"error type");
        }
        if(collectService.collect(type,articleId,userId)){
            LogUtils.log(username+" collect article id " + articleId + ", type is " + type,"collect",false,request);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"exist");
        }
    }

    /**
     * 我的资讯（收藏）
     * @param request
     * @return
     */
    @RequestMapping(value = "/myInformation",method = RequestMethod.GET)
    public Object myInformation(HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,collectService.findCollect(1,userId));
    }

    /**
     * 我的法律（收藏）
     * @param request
     * @return
     */
    @RequestMapping(value = "/myLaw",method = RequestMethod.GET)
    public Object myLaw(HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,collectService.findCollect(2,userId));
    }

    /**
     * 我的新规（收藏）
     * @param request
     * @return
     */
    @RequestMapping(value = "/myRule",method = RequestMethod.GET)
    public Object myRule(HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,collectService.findCollect(3,userId));
    }
    /**
     * 根据id和type删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int type,@RequestParam int id,HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        if (type == 1){
            collectService.deleteCollect(1,id,userId);
        }else if (type == 2){
            collectService.deleteCollect(2,id,userId);
        }else if (type == 3){
            collectService.deleteCollect(3,id,userId);
        }else{
            return new ResultBody<>(false,500,"error id");
        }
        return new ResultBody<>(true,200,null);
    }

    /**
     * 删除
     * @param id 资讯的id
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteMyInformation",method = RequestMethod.GET)
    public Object deleteInformation(@RequestParam int id,HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        if(id <= 0){
            return new ResultBody<>(false,501,"error id");
        }
        collectService.deleteCollect(1,id,userId);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 删除
     * @param id 法律的id
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteMyLaw",method = RequestMethod.GET)
    public Object deleteLaw(@RequestParam int id,HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        if(id <= 0){
            return new ResultBody<>(false,501,"error id");
        }
        collectService.deleteCollect(2,id,userId);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 删除
     * @param id 新规的id
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteMyRule",method = RequestMethod.GET)
    public Object deleteRule(@RequestParam int id,HttpServletRequest request){
        int userId = UserUtils.getUserId(request,jwtConfig);
        if(id <= 0){
            return new ResultBody<>(false,501,"error id");
        }
        collectService.deleteCollect(3,id,userId);
        return new ResultBody<>(true,200,null);
    }
}
