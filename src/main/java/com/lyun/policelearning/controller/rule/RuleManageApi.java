package com.lyun.policelearning.controller.rule;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.service.RuleService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/rule/manage")
public class RuleManageApi {
    @Autowired
    RuleService ruleService;
    /**
     * 根据传入的PageRequest对象，返回分页之后的信息
     * @param pageQuery
     * @return
     */
    @RequestMapping(value="/getPage",method = RequestMethod.POST)
    public Object findPage(@RequestBody PageRequest pageQuery) {
        if(pageQuery.getPageSize() <= 0||pageQuery.getPageNum()<=0){
            return new ResultBody<>(false,500,"error pageSize or error pageNum");
        }
        return new ResultBody<>(true,200,ruleService.findPage(pageQuery));
    }


    /**
     * 根据id返回新规的具体内容
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPage/content",method = RequestMethod.GET)
    public Object getRuleById(@RequestParam int id){
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

    /**
     * 根据传入rule对象，对数据库进行插入操作
     * @param rule
     * @return
     */
    @RequestMapping(value = "getPage/insert",method = RequestMethod.POST)
    public Object insertRule(@RequestBody Rule rule){
        if(ruleService.insertRule(rule)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"can't insert");
        }
    }

    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    @RequestMapping(value = "getPage/delete",method = RequestMethod.GET)
    public Object deleteById(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }if(ruleService.deleteById(id)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"can't delete");
        }
    }

    /**
     * 根据id和传入的rule更新rule
     * @param rule
     * @return
     */
    @RequestMapping(value = "getPage/content/update",method = RequestMethod.POST)
    public Object updateById(@RequestBody Rule rule){
        System.out.println(rule.getTitle());
        if(rule == null){
            return new ResultBody<>(false,500,"error id or error rule");
        }if(ruleService.updateById(rule)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"can't update");
        }
    }
}
