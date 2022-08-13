package com.lyun.policelearning.controller.rule;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.CollectService;
import com.lyun.policelearning.service.RuleService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleApi {
    @Autowired
    RuleService ruleService;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    CollectService collectService;

    /**
     * 根据起始和长度返回新规
     * @return 返回新规
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object findAll(){
        List<JSONObject> rulelist = ruleService.findAll();
        return new ResultBody<>(true,200,rulelist);
    }

    /**
     * 根据id返回新规的具体内容
     * @param id
     * @return
     */
    @RequestMapping(value = "/content",method = RequestMethod.GET)
    public Object getRuleById(@RequestParam int id, HttpServletRequest request){
        //更新数据库中的view值
        ruleService.updateView(id);
        int userId = UserUtils.getUserId(request,jwtConfig);
        JSONObject res = ruleService.getRuleById(id);
        if (collectService.isCollect(3,id,userId)){
            res.put("isCollect",true);
        }else {
            res.put("isCollect",false);
        }
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
