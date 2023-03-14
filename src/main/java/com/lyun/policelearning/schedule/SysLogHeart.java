package com.lyun.policelearning.schedule;

import com.lyun.policelearning.utils.log.AuditSysLogSender;
import org.springframework.scheduling.annotation.Scheduled;

public class SysLogHeart {

    /**
     * 每四小时发送一次心跳连接
     */
    @Scheduled(fixedRate = 4 * 60 * 60 * 1000)
    public void heart(){
        AuditSysLogSender.getInstance().heart();
    }

}
