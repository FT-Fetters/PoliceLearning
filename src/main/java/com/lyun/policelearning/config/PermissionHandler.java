package com.lyun.policelearning.config;

import com.lyun.policelearning.utils.ResultBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.security.SignatureException;

@RestControllerAdvice
public class PermissionHandler {

    @ExceptionHandler(value = {SignatureException.class})
    @ResponseBody
    public ResultBody<?> authorizationException(SignatureException e){
        return new ResultBody<>(false,-1,e.getMessage());
    }
}
