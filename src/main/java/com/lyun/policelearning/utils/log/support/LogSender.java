package com.lyun.policelearning.utils.log.support;

import com.lyun.policelearning.entity.SysLog;

public interface LogSender {

    void send(SysLog sysLog);

    void heart();

}
