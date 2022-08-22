package com.lyun.policelearning.controller.paper;

import com.lyun.policelearning.entity.SchedulePaper;
import com.lyun.policelearning.schedule.ScheduledFutureHolder;
import com.lyun.policelearning.schedule.task.PaperTask;
import com.lyun.policelearning.service.paper.PaperService;
import com.lyun.policelearning.service.paper.SchedulePaperService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/schedule/paper")
public class SchedulePaperApi {
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private SchedulePaperService schedulePaperService;

    @Autowired
    private PaperService paperService;

    private HashMap<Long, ScheduledFutureHolder> scheduleMap = new HashMap<>();


    @PostConstruct
    public void init(){
        schedulePaperService.init(scheduleMap,threadPoolTaskScheduler);
    }

    @PostMapping("/insert")
    public Object insert(@RequestBody SchedulePaper schedulePaper){
        return new ResultBody<>(true,200,schedulePaperService.insert(schedulePaper,scheduleMap,threadPoolTaskScheduler));
    }

    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable Long id){
        stopTask(id);
        scheduleMap.remove(id);
        schedulePaperService.delete(id);
        return new ResultBody<>(true,200,null);
    }

    @PostMapping("/update")
    public Object update(@RequestBody SchedulePaper schedulePaper){
        schedulePaperService.update(schedulePaper);
        restart(schedulePaper.getId());
        return new ResultBody<>(true,200,null);
    }


    @PostMapping("/start/{id}")
    public Object startTask(@PathVariable Long id){
        SchedulePaper schedulePaper = schedulePaperService.selectById(id);
        ScheduledFutureHolder scheduledFutureHolder = scheduleMap.get(id);
        PaperTask paperTask = new PaperTask();
        paperTask.setSchedulePaper(schedulePaper);
        paperTask.setPaperService(paperService);
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(paperTask,new CronTrigger(schedulePaper.getCron()));
        scheduledFutureHolder.setRunning(true);
        scheduledFutureHolder.setScheduledFuture(scheduledFuture);
        return new ResultBody<>(true,200,null);
    }

    @PostMapping("/list")
    public Object queryTask(@RequestBody PageRequest pageRequest){
        List<ScheduledFutureHolder> list = new ArrayList<>();
        for (Long id : scheduleMap.keySet()) {
            list.add(scheduleMap.get(id));
        }
        PageResult page = PageUtil.getPage(pageRequest, list);
        return new ResultBody<>(true,200,page);
    }

    @PostMapping("/stop/{id}")
    public Object stopTask(@PathVariable Long id){
        if (scheduleMap.containsKey(id)){
            ScheduledFutureHolder scheduledFutureHolder = scheduleMap.get(id);
            ScheduledFuture<?> scheduledFuture = scheduledFutureHolder.getScheduledFuture();
            if (scheduledFuture!=null){
                scheduledFuture.cancel(true);
                scheduledFutureHolder.setRunning(false);
            }
        }
        return new ResultBody<>(true,200,null);
    }


    @SneakyThrows
    @PostMapping("/restart/{id}")
    public Object restart(@PathVariable Long id){
        if (scheduleMap.containsKey(id)){
            ScheduledFutureHolder scheduledFutureHolder = scheduleMap.get(id);
            ScheduledFuture<?> scheduledFuture = scheduledFutureHolder.getScheduledFuture();
            if (scheduledFuture!=null){
                scheduledFuture.cancel(true);
                PaperTask paperTask = (PaperTask) scheduledFutureHolder.getRunnableClass().newInstance();
                SchedulePaper schedulePaper = schedulePaperService.selectById(id);
                paperTask.setSchedulePaper(schedulePaper);
                paperTask.setPaperService(paperService);
                scheduledFutureHolder.setRunning(true);
                ScheduledFuture<?> tmp = threadPoolTaskScheduler.schedule(paperTask, new CronTrigger(schedulePaper.getCron()));
                scheduledFutureHolder.setScheduledFuture(tmp);
                scheduledFutureHolder.setSchedulePaper(schedulePaper);
                scheduleMap.put(id,scheduledFutureHolder);
            }
        }
        return new ResultBody<>(true,200,null);
    }




}
