package com.lyun.policelearning.utils.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.SysLog;
import com.lyun.policelearning.utils.factory.AuditSysLogFactory;
import com.lyun.policelearning.utils.factory.support.LogFactory;
import com.lyun.policelearning.utils.log.support.LogSender;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditSysLogSender implements LogSender {

    private static final String URL_PREFIX = "https://44.10.1.134/api";
    private static final String URL_HEARTBEAT = "/kafka/heartbeat";
    private static final String URL_ACCEPT_LOGS = "/kafka/acceptLogs";
    private static final String[] HEADER_KEYS = new String[]{
            HttpHeaders.CONTENT_TYPE, "sysId", "verifyCode", "debug"
    };
    private static final String[] HEADER_VALUES = new String[]{
            MediaType.APPLICATION_JSON_VALUE, "S1678348162540", "af2faf20" , "true"
    };

    private volatile static LogSender instance = null;

    public static LogSender getInstance() {
        if (instance == null){
            synchronized (AuditSysLogFactory.class){
                if (instance == null){
                    instance = new AuditSysLogSender();
                }
            }
        }
        return instance;
    }

    private static final Integer QUEUE_MAX_SIZE = 5;

    private final List<SysLog> logQueue = new ArrayList<>();



    @Override
    public void send(SysLog sysLog) {
        if (logQueue.size() > QUEUE_MAX_SIZE){
            this.send();
        }else {
            logQueue.add(sysLog);
        }
    }

    @Override
    public void heart() {
        HttpHeaders headers = new HttpHeaders();
        for (int i = 0; i < HEADER_KEYS.length; i++) {
            headers.add(HEADER_KEYS[i],HEADER_VALUES[i]);
        }
        RestTemplate restTemplate = new RestTemplate();
        JSONObject result = restTemplate.postForObject(URL_PREFIX + URL_HEARTBEAT, new HttpEntity<String>(headers), JSONObject.class);
    }

    private void send(){
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("Logs", logQueue);
        String body = bodyJson.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        for (int i = 0; i < HEADER_KEYS.length; i++) {
            headers.add(HEADER_KEYS[i],HEADER_VALUES[i]);
        }
        RestTemplate restTemplate = new RestTemplate();
        JSONObject result = restTemplate.postForObject(URL_PREFIX + URL_ACCEPT_LOGS, new HttpEntity<>(new HashMap<>(), headers), JSONObject.class);
        //JSONObject result = restTemplate.postForObject(url, new HttpEntity<>(new HashMap<>(),headers), JSONObject.class);
        assert result != null;
        Assert.assertFalse( result.getBoolean("success"));
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), result.get("code"));

        result = restTemplate.postForObject(URL_PREFIX + URL_ACCEPT_LOGS, new HttpEntity<>(body, headers), JSONObject.class);
        //result = restTemplate.postForObject(url, new HttpEntity<>(body,headers), JSONObject.class);
        assert result != null;
        Assert.assertFalse( result.getBoolean("success"));
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), result.get("code"));

        headers.add("batchNumber", "5");
        result = restTemplate.postForObject(URL_PREFIX + URL_ACCEPT_LOGS, new HttpEntity<>(body, headers), JSONObject.class);
        assert result != null;
        Assert.assertFalse(result.getBoolean("success"));
        Assert.assertEquals(HttpStatus.PAYMENT_REQUIRED.value(), result.get("code"));

        result = restTemplate.postForObject(URL_PREFIX + URL_ACCEPT_LOGS, new HttpEntity<>(body, headers), JSONObject.class);
        assert result != null;
        Assert.assertTrue(result.getBoolean("success"));
        Assert.assertEquals(HttpStatus.OK.value(), result.get("code"));
        logQueue.clear();
    }


}
