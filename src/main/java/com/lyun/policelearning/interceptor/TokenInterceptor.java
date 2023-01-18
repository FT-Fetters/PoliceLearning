package com.lyun.policelearning.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.Role;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Map;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws SignatureException {
        /* 请求输出 */
        String uri = request.getRequestURI() ;
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (int i = 0; i < uri.length()*2; i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.println(new Time(System.currentTimeMillis()));
        System.out.println("访问地址:"+uri);
        System.out.println("参数:");
        for (String s : parameterMap.keySet()) {
            System.out.println(s+":" + Arrays.toString(parameterMap.get(s)));
        }
        for (int i = 0; i < uri.length()*2; i++) {
            System.out.print("-");
        }
        System.out.println();
        /* 地址过滤 */
        if (uri.contains("/") || uri.contains("/login") || uri.contains("/video") ||
                uri.contains("/course/all") || uri.contains("/test/run") ||
                uri.contains("/pki") || uri.contains("/api") ||
                uri.contains("/download") || uri.contains("/export")
        ){
            return true ;
        }
        /* Token 验证 */
        String token = request.getHeader(jwtConfig.getHeader());
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        if(StringUtils.isEmpty(token)){
            throw new SignatureException(jwtConfig.getHeader()+ "不能为空");
        }

        Claims claims = null;
        try{
            claims = jwtConfig.getTokenClaim(token);
            if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
                throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
            }
        }catch (Exception e){
            throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
        }

        /* 设置 identityId 用户身份ID */
        request.setAttribute("identityId", claims.getSubject());
        /* 权限判断 */
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //类注解
            Permission cla_ann = handlerMethod.getBeanType().getAnnotation(Permission.class);
            //方法注解
            Permission met_ann = handlerMethod.getMethodAnnotation(Permission.class);
            if (cla_ann != null || met_ann !=null){
                if ((cla_ann!=null && cla_ann.admin()) || (met_ann!=null && met_ann.admin())){
                    if (met_ann!=null && !met_ann.admin())
                        return true;
                    User user = userService.getById(UserUtils.getUserId(request, jwtConfig));
                    Role role = roleService.findById(user.getRole());
                    if (role == null){
                        throw new SignatureException("该用户的角色已被删除，无法授权");
                    }
                    if (!role.isAdmin()){
                        throw new SignatureException("用户 " + UserUtils.getUsername(request,jwtConfig) +"访问路径:" + uri + "权限不足，需要管理员权限");
                    }
                }
            }
        }
        return true;
    }
}
