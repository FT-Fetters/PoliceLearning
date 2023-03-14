package com.lyun.policelearning.aspect;

import com.lyun.policelearning.annotation.SysLogAnnotation;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.SysLog;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.utils.UserUtils;
import com.lyun.policelearning.utils.factory.AuditSysLogFactory;
import com.lyun.policelearning.utils.log.AuditSysLogSender;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
public class SysLogAspect {

    @Autowired
    private JwtConfig jwtConfig;

    @Pointcut("@annotation(com.lyun.policelearning.annotation.SysLogAnnotation)")
    public void opLogPointCut(){

    }

    @AfterReturning(returning = "result",value = "opLogPointCut()")
    public void logProcessing(JoinPoint joinPoint, Object result){
        new Thread(() -> {
            try {
                MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
                Method method = signature.getMethod();
                SysLogAnnotation annotation = method.getAnnotation(SysLogAnnotation.class);
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                assert requestAttributes != null;
                HttpServletRequest request = null;
                User user = null;
                if (annotation != null){
                    if (!annotation.opType().equals("登录")){
                        request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
                        assert request != null;
                        user = UserUtils.getUser(request, jwtConfig);
                    }
                    SysLog log = AuditSysLogFactory.getInstance().getLog(result, request, annotation, user);
                    AuditSysLogSender.getInstance().send(log);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}

