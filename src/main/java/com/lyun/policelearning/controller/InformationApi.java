package com.lyun.policelearning.controller;


import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/information")
public class InformationApi {

    @Autowired
    InformationService informationService;

    /**
     * 获取资讯列表
     * @param start 开始的位置
     * @param len 要获取的资讯列表的长度
     * @return 200获取成功,500参数错误
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object GetList(@RequestParam int start,@RequestParam int len){
        List<Information> informationList = informationService.findAll();
        if (start < 0 || start > informationList.size() - 1){
            return new ResultBody<>(false,500,"error start");
        }
        if (len < 0 || start + len > informationList.size()){
            return new ResultBody<>(false,500,"error len");
        }
        List<Information> res = informationList.subList(start,start+len);
        return new ResultBody<>(true,200,res);
    }

}
