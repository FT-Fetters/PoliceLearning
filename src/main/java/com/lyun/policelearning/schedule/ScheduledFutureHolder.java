package com.lyun.policelearning.schedule;

import com.lyun.policelearning.entity.SchedulePaper;
import lombok.Data;

import java.util.concurrent.ScheduledFuture;

@Data
public class ScheduledFutureHolder {
    private ScheduledFuture<?> scheduledFuture;

    private Class<? extends Runnable> runnableClass;

    private SchedulePaper schedulePaper;

    private boolean running;
}
