package com.lyun.policelearning.controller.information;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.service.RuleService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/information/manage")
public class InformationManageApi {
    @Autowired
    InformationService informationService;

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
        return new ResultBody<>(true,200,informationService.findPage(pageQuery));
    }


    /**
     * 根据id返回资讯的具体内容
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPage/content",method = RequestMethod.GET)
    public Object getRuleById(@RequestParam int id){
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
     * 根据传入information对象，对数据库进行插入操作
     * @param information
     * @return
     */
    @RequestMapping(value = "/getPage/insert",method = RequestMethod.POST)
    public Object insertInformation(@RequestBody Information information){
        if(informationService.insertInformation(information)){
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
    @RequestMapping(value = "/getPage/delete",method = RequestMethod.GET)
    public Object deleteById(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }if(informationService.deleteById(id)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"can't delete");
        }
    }

    /**
     * 根据传入的information更新information
     * @param information
     * @return
     */
    @RequestMapping(value = "/getPage/update",method = RequestMethod.POST)
    public Object updateById(@RequestBody Information information){
        if(information == null){
            return new ResultBody<>(false,500,"error id or error rule");
        }if(informationService.updateById(information)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"can't update");
        }
    }

    /**
     * 实现置顶和取消置顶的功能
     * @param id
     * @param istop 1为置顶  0为不置顶
     * @return
     */
    @RequestMapping(value = "/getPage/setTop",method = RequestMethod.GET)
    public Object setTop(@RequestParam int id,@RequestParam int istop){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if(istop != 1 && istop != 0){
            return new ResultBody<>(false,500,"error istop");
        }
        if(informationService.updateTopById(id,istop)){
            return new ResultBody<>(true,200,null);
        }else{
            return new ResultBody<>(false,501,"can't update");
        }
    }
}
