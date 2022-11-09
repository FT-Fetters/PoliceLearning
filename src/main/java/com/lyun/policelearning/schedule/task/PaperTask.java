package com.lyun.policelearning.schedule.task;

import com.lyun.policelearning.controller.paper.model.GeneratePaperBody;
import com.lyun.policelearning.entity.SchedulePaper;
import com.lyun.policelearning.service.paper.PaperService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaperTask implements Runnable{
    private SchedulePaper schedulePaper;

    private PaperService paperService;

    public void setSchedulePaper(SchedulePaper schedulePaper) {
        this.schedulePaper = schedulePaper;
    }

    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    @Override
    public void run() {
        if (schedulePaper!=null){
            String title = schedulePaper.getTitle();
            title = title.replace("{year}",new SimpleDateFormat("yyyy").format(new Date()));
            title = title.replace("{week}",String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)));
            title = title.replace("{month}",String.valueOf(Calendar.getInstance().get(Calendar.MONTH)));
            GeneratePaperBody body = new GeneratePaperBody();
            body.setTitle(title);
            body.setJ(schedulePaper.getJ());
            body.setS(schedulePaper.getS());
            body.setM(schedulePaper.getM());
            body.setScore_j(Integer.valueOf(schedulePaper.getScore().split(",")[0]));
            body.setScore_s(Integer.valueOf(schedulePaper.getScore().split(",")[1]));
            body.setScore_m(Integer.valueOf(schedulePaper.getScore().split(",")[2]));
            body.setTime(schedulePaper.getTime());
            body.setUid(1);
            int id = paperService.generate(body);
            System.out.println("学法考试新建成功，id:" + id);
        }
    }
}
