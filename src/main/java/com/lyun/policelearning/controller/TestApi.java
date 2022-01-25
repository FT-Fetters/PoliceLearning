package com.lyun.policelearning.controller;

import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/test")
public class TestApi {
    @RequestMapping(value = "/run",method = RequestMethod.GET)
    public Object run(){
        return new ResultBody<>(true,200, PathTools.getRunPath());
    }
}
