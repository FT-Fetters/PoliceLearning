package com.lyun.policelearning.controller.information;


import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/information")
public class InformationApi {

    @Autowired
    InformationService informationService;

    /**
     * 根据起始和长度返回资讯
     * @param start 开始  可以从0开始
     * @param len   返回的个数
     * @return 返回资讯
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object findAll(@RequestParam int start, @RequestParam int len) throws Exception {
        List<JSONObject> informationList = informationService.findAll();
        if (start < 0 || start > informationList.size() - 1){
            return new ResultBody<>(false,500,"error start");
        }
        if (len < 0 || start + len > informationList.size()){
            return new ResultBody<>(false,500,"error len");
        }
        List<JSONObject> res = informationList.subList(start,start+len);
        return new ResultBody<>(true,200,res);
    }

    /**
     * 根据id返回资讯的具体内容
     * @param id
     * @return
     */
    @RequestMapping(value = "/content",method = RequestMethod.GET)
    public Object getInformationById(@RequestParam int id) throws Exception {
        //更新数据库中的view值
        informationService.updateView(id);
        JSONObject res = informationService.getInformationById(id);
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
