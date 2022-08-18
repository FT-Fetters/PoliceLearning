package com.lyun.policelearning.service.paper;

import com.lyun.policelearning.entity.SchedulePaper;
import com.lyun.policelearning.schedule.ScheduledFutureHolder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;


public interface SchedulePaperService {
    void init(HashMap<Long, ScheduledFutureHolder> scheduleMap, ThreadPoolTaskScheduler threadPoolTaskScheduler);
    SchedulePaper selectById(Long id);
    Long insert(SchedulePaper schedulePaper, HashMap<Long, ScheduledFutureHolder> scheduleMap, ThreadPoolTaskScheduler threadPoolTaskScheduler);
    void delete(Long id);
    void update(SchedulePaper schedulePaper);
}
