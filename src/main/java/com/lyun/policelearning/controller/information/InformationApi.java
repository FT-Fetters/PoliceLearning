package com.lyun.policelearning.controller.information;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.SysLogAnnotation;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.CollectService;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/information")
public class InformationApi {

    @Autowired
    InformationService informationService;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    CollectService collectService;

    /**
     * 根据起始和长度返回资讯
     * @return 返回资讯
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "咨询模块", opDesc = "用户获取所有咨询", opType = "插入")
    public Object findAll() throws Exception {
        List<JSONObject> informationList = informationService.findAll();
        return new ResultBody<>(true,200,informationList);
    }

    /**
     * 根据id返回资讯的具体内容
     * @param id
     * @return
     */
    @RequestMapping(value = "/content",method = RequestMethod.GET)
    @SysLogAnnotation(opModel = "咨询模块", opDesc = "用户获取指定咨询内容", opType = "查询")
    public Object getInformationById(@RequestParam int id, HttpServletRequest request) throws Exception {
        //更新数据库中的view值
        informationService.updateView(id);
        JSONObject res = informationService.getInformationById(id);
        //获取用户的id  以及获取文章的id  传入类型进行查询 结果不为null 则isCollect=true
        int userId = UserUtils.getUserId(request,jwtConfig);
        res.put("isCollect",collectService.isCollect(1,id,userId));
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if(id > 0){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"no found");
        }
    }
    /**
     * 返回三个从资讯中获取的轮播图
     * @return
     */
    @RequestMapping(value = "/getPicture",method = RequestMethod.GET)
    public Object getPicture() throws Exception {
        return new ResultBody<>(true,200,informationService.getPicture());
    }
}
