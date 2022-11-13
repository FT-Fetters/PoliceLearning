package com.lyun.policelearning.dao.paper;

import com.lyun.policelearning.entity.SchedulePaper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SchedulePaperDao {

    @Select("select * from `schedule_paper`")
    List<SchedulePaper> selectAll();

    @Select("select * from `schedule_paper` where id=#{id}")
    SchedulePaper selectById(Long id);

    @Delete("delete from `schedule_paper` where id=#{id}")
    int deleteById(Long id);

    @Update("update schedule_paper set cron=#{cron}, j=#{j}, s=#{s}, m=#{m}, title=#{title}, score=#{score}, `time`=#{time} where id=#{id}")
    int update(SchedulePaper schedulePaper);

    int insert(SchedulePaper schedulePaper);
}
