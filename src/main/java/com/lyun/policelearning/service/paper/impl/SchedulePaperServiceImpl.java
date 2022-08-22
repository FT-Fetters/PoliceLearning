package com.lyun.policelearning.service.paper.impl;

import com.lyun.policelearning.dao.paper.SchedulePaperDao;
import com.lyun.policelearning.entity.SchedulePaper;
import com.lyun.policelearning.schedule.ScheduledFutureHolder;
import com.lyun.policelearning.schedule.task.PaperTask;
import com.lyun.policelearning.service.paper.PaperService;
import com.lyun.policelearning.service.paper.SchedulePaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulePaperServiceImpl implements SchedulePaperService {

    @Autowired
    private SchedulePaperDao schedulePaperDao;

    @Autowired
    private PaperService paperService;



    @Override
    public void init(HashMap<Long, ScheduledFutureHolder> scheduleMap, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        List<SchedulePaper> list = schedulePaperDao.selectAll();
        for (SchedulePaper schedulePaper : list) {
            runTask(schedulePaper,scheduleMap,threadPoolTaskScheduler);
        }
    }

    @Override
    public SchedulePaper selectById(Long id) {
        return schedulePaperDao.selectById(id);
    }

    @Override
    public Long insert(SchedulePaper schedulePaper, HashMap<Long, ScheduledFutureHolder> scheduleMap, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        if (Integer.parseInt(schedulePaper.getCron()) >= 1 && Integer.parseInt(schedulePaper.getCron()) <= 7){
            schedulePaper.setCron("0 0 0 ? * "+Integer.parseInt(schedulePaper.getCron()));
        }else return -1L;
        schedulePaperDao.insert(schedulePaper);
        runTask(schedulePaper, scheduleMap, threadPoolTaskScheduler);
        return schedulePaper.getId();
    }

    @Override
    public void delete(Long id) {
        schedulePaperDao.deleteById(id);
    }

    @Override
    public void update(SchedulePaper schedulePaper) {
        if (Integer.parseInt(schedulePaper.getCron()) >= 1 && Integer.parseInt(schedulePaper.getCron()) <= 7){
            schedulePaper.setCron("0 0 0 ? * "+Integer.parseInt(schedulePaper.getCron()));
        }
        schedulePaperDao.update(schedulePaper);
    }


    private void runTask(SchedulePaper schedulePaper,HashMap<Long, ScheduledFutureHolder> scheduleMap, ThreadPoolTaskScheduler threadPoolTaskScheduler){
        PaperTask paperTask = new PaperTask();
        paperTask.setSchedulePaper(schedulePaper);
        paperTask.setPaperService(paperService);
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(paperTask, new CronTrigger(schedulePaper.getCron()));
        ScheduledFutureHolder scheduledFutureHolder = new ScheduledFutureHolder();
        scheduledFutureHolder.setScheduledFuture(scheduledFuture);
        scheduledFutureHolder.setRunnableClass(paperTask.getClass());
        scheduledFutureHolder.setSchedulePaper(schedulePaper);
        scheduledFutureHolder.setRunning(true);
        scheduleMap.put(schedulePaper.getId(),scheduledFutureHolder);
    }
}
