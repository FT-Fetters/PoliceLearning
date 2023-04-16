package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.course.CourseContentDao;
import com.lyun.policelearning.dao.course.CourseDao;
import com.lyun.policelearning.dao.course.CourseUsrLearnDao;
import com.lyun.policelearning.entity.course.Course;
import com.lyun.policelearning.entity.course.CourseContent;
import com.lyun.policelearning.entity.course.CourseUsrLearn;
import com.lyun.policelearning.service.CourseUsrLearnService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseUsrLearnServiceImpl implements CourseUsrLearnService {


    @Autowired
    private CourseUsrLearnDao courseUsrLearnDao;

    @Autowired
    private CourseContentDao courseContentDao;

    @Autowired
    private CourseDao courseDao;


    @Override
    public ResultBody<?> submitLearnTime(long learnTime, long courseId, long contentId, long userId) {
        CourseUsrLearn courseUsrLearn = courseUsrLearnDao.queryOne(userId, courseId, contentId);
        if (courseUsrLearn == null) {
            CourseContent courseContent = courseContentDao.getById(contentId);
            Course course = courseDao.getCourseById(courseId);
            if (courseContent == null || course == null)
                return new ResultBody<>(true,200, "课程或课程内容不存在");
            CourseUsrLearn build = CourseUsrLearn.builder()
                    .courseId(courseId).userId(userId)
                    .contentId(contentId).learnTime(learnTime)
                    .build();
            courseUsrLearnDao.insert(build);
            return new ResultBody<>(true,200,build.getId());
        }else {
            courseUsrLearn.setLearnTime(courseUsrLearn.getLearnTime() + learnTime);
            courseUsrLearnDao.update(courseUsrLearn);
            return new ResultBody<>(true,200,courseUsrLearn.getId());
        }
    }

    @Override
    public ResultBody<?> checkFinished(long courseId, long contentId, long userId) {
        CourseUsrLearn courseUsrLearn = courseUsrLearnDao.queryOne(courseId, contentId, userId);
        if (courseUsrLearn == null){
            return new ResultBody<>(true,200,false);
        }else {
            CourseContent courseContent = courseContentDao.getById(contentId);
            if (courseContent == null) return new ResultBody<>(true,200,false);
            if (courseUsrLearn.getLearnTime() >= courseContent.getPlanTime())
                return new ResultBody<>(true,200,true);
            else
                return new ResultBody<>(true,200,false);

        }
    }
}
