package com.lyun.policelearning.controller.tool;

import com.lyun.policelearning.service.ToolTypeService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tool/type")
public class ToolTypeManageApi {
    @Autowired
    ToolTypeService toolTypeService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object all(){
        return new ResultBody<>(true,200,toolTypeService.all());
    }

    /**
     * 删除工具类型
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam Integer id){
        toolTypeService.delete(id);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 新增工具类型
     */
    @RequestMapping(value = "/insert",method = RequestMethod.GET)
    public Object insert(@RequestParam String name){
        toolTypeService.insert(name);
        return new ResultBody<>(true,200,null);
    }
}
