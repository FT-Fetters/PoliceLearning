package com.lyun.policelearning.controller.tool;

import com.lyun.policelearning.service.ToolService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool")
public class ToolApi {

    @Autowired
    ToolService toolService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object getAll(@RequestParam int type,@RequestParam(required = false) String title){
        return new ResultBody<>(true,200,toolService.list(type,title));
    }

    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public Object getById(@RequestParam int id){
        return new ResultBody<>(true,200,toolService.getById(id));
    }
}
