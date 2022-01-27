package com.lyun.policelearning.controller;

import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestApi {
    @RequestMapping(value = "/run",method = RequestMethod.GET)
    public Object run(){
        try {
            throw new Exception("exp");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getStackTrace();
        }
        //return new ResultBody<>(true,200, PathTools.getRunPath());
    }
}
