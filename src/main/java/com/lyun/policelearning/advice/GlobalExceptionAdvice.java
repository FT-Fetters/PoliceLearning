package com.lyun.policelearning.advice;

import com.lyun.policelearning.utils.LogUtils;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SignatureException;
import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object handleHttpException(HttpServletRequest req, Exception ex){
        ex.printStackTrace();
        LogUtils.log(ex.getMessage(),"error",false,null);
        return new ResultBody<>(false,-1,ex.getMessage());
    }


}
