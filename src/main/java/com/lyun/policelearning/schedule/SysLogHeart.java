package com.lyun.policelearning.schedule;

import com.lyun.policelearning.utils.log.AuditSysLogSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SysLogHeart {

    /**
     * 每四小时发送一次心跳连接
     */
    @Scheduled(fixedRate = 4 * 60 * 60 * 1000)
    public void heart(){
        AuditSysLogSender.getInstance().heart();
    }

}
