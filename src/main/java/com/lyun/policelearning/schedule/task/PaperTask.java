package com.lyun.policelearning.schedule.task;

import com.alibaba.druid.support.spring.stat.SpringStatUtils;
import com.lyun.policelearning.controller.paper.PaperApi;
import com.lyun.policelearning.entity.SchedulePaper;
import com.lyun.policelearning.service.paper.PaperService;
import com.lyun.policelearning.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaperTask implements Runnable{
    private SchedulePaper schedulePaper;

    private PaperService paperService = SpringUtil.getBean(PaperService.class);

    public void setSchedulePaper(SchedulePaper schedulePaper) {
        this.schedulePaper = schedulePaper;
    }

    @Override
    public void run() {
        if (schedulePaper!=null){
            String title = schedulePaper.getTitle();
            title = title.replace("{year}",new SimpleDateFormat("yyyy").format(new Date()));
            title = title.replace("{week}",String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)));
            title = title.replace("{month}",String.valueOf(Calendar.getInstance().get(Calendar.MONTH)));
            int id = paperService.generate(schedulePaper.getJ(),schedulePaper.getM(),schedulePaper.getS(),title,1);
            System.out.println("学法考试新建成功，id:" + id);
        }
    }
}
