package com.lyun.policelearning.controller.law;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.LawService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/law")
public class LawApi {
    @Autowired
    LawService lawService;
    /**
     * 获取所有的法律类型
     * @return 返回法律类型这一列表
     */
    @RequestMapping("")
    public Object getLawType(){
        return new ResultBody<>(true,200,lawService.findAllType());
    }
    @RequestMapping("/all")
    public Object getAllLawType(){
        return new ResultBody<>(true,200,lawService.findAll());
    }
    @RequestMapping(value = "/catalogue",method = RequestMethod.GET)
    public Object getCatalogueByType(@RequestParam String lawtype){
        if(lawtype == null){
            return new ResultBody<>(false,500,"error type");
        }
        JSONObject res = lawService.findTitleByName(lawtype);
        if (res != null){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"unknown type");
        }
    }
}
