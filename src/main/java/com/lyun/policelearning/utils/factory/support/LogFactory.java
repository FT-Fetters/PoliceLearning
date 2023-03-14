package com.lyun.policelearning.utils.factory.support;

import com.lyun.policelearning.annotation.SysLogAnnotation;
import com.lyun.policelearning.entity.SysLog;
import com.lyun.policelearning.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface LogFactory {
    SysLog getLog(Object result, HttpServletRequest request, SysLogAnnotation annotation, User user);

}
