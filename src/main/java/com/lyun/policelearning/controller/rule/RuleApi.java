package com.lyun.policelearning.controller.rule;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.service.RuleService;
import com.lyun.policelearning.service.RuleServiceImpl;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleApi {
    @Autowired
    RuleService ruleService;

    /**
     * 根据起始和长度返回新规
     * @param start 开始  可以从0开始
     * @param len   返回的个数
     * @return 返回新规
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object findAll(@RequestParam int start, @RequestParam int len){
        List<JSONObject> rulelist = ruleService.findAll();
        int total = rulelist.size();
        if (start < 0 || start > rulelist.size() - 1){
            return new ResultBody<>(false,500,"error start");
        }
        if (len < 0 || start + len > rulelist.size()){
            return new ResultBody<>(false,500,"error len");
        }
        JSONObject  jsonObject = new JSONObject();
        List<JSONObject> res = rulelist.subList(start,start+len);
        jsonObject.put("rule",res);
        jsonObject.put("size",total);
        return new ResultBody<>(true,200,jsonObject);
    }

    /**
     * 根据id返回新规的具体内容
     * @param id
     * @return
     */
    @RequestMapping(value = "/content",method = RequestMethod.GET)
    public Object getRuleById(@RequestParam int id){
        //更新数据库中的view值
        ruleService.updateView(id);

        JSONObject res = ruleService.getRuleById(id);
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if(id > 0){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"no found");
        }
    }
}
