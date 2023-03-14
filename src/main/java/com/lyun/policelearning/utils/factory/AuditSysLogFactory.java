package com.lyun.policelearning.utils.factory;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.SysLogAnnotation;
import com.lyun.policelearning.entity.SysLog;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.factory.support.LogFactory;
import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

public class AuditSysLogFactory implements LogFactory {

    /**
     * 组织名
     */
    private static final String ORGANIZATION = "龙岩市公安局新罗分局";
    /**
     * 组织id
     */
    private static final String ORGANIZATION_ID = "350802000000";


    private volatile static LogFactory instance = null;

    public static synchronized LogFactory getInstance() {
        if (instance == null) {
            synchronized (AuditSysLogFactory.class) {
                if (instance == null) {
                    instance = new AuditSysLogFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public SysLog getLog(Object result, HttpServletRequest request, SysLogAnnotation annotation, User user) {
        ResultBody<?> resultBody = null;
        JSONObject bodyJson = null;
        if (result.getClass() == ResultBody.class) {
            resultBody = (ResultBody<?>) result;
            bodyJson = (JSONObject) JSONObject.toJSON(resultBody);
        }

        assert bodyJson != null;
        return SysLog.builder()
                .operateTime(new Timestamp(System.currentTimeMillis()).toString())
                .redId("S1678348162540")
                .operateResult(resultBody.getBody() != null ? 1 : 0)
                .operateType(getOpType(annotation.opType()))
                .numId(new Snowflake().nextId())
                .username(user == null ? "" : user.getUsername())
                .userType("00")
                .userId(user == null ? "" : String.valueOf(user.getId()))
                .organization(ORGANIZATION)
                .organizationId(ORGANIZATION_ID)
                .terminalType("10")
                .terminalIp(request == null ? "0.0.0.0" : ServletUtil.getClientIP(request))
                .inquireType(resultBody.getBody().getClass().getName())
                .inquireContent(
                        bodyJson.toJSONString().length() >= 200
                                ? bodyJson.toJSONString().substring(0,200)
                                : bodyJson.toJSONString())
                .recordType("operation").build();
    }

    private Integer getOpType(String type){
        switch (type){
            case "登录":
                return 0;
            case "查询":
                return 1;
            case "新增":
                return 2;
            case "修改":
                return 3;
            case "删除":
                return 4;
            default:
                return -1;
        }
    }
}
