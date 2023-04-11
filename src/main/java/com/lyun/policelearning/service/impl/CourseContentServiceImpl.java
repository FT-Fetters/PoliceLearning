package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.CourseContentDao;
import com.lyun.policelearning.entity.CourseContent;
import com.lyun.policelearning.service.CourseContentService;
import com.lyun.policelearning.service.StateService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseContentServiceImpl implements CourseContentService {

    @Autowired
    private StateService stateService;

    @Autowired
    private CourseContentDao courseContentDao;

    @Override
    public ResultBody<?> getById(int id, int userId) {
        if (!stateService.check(id, userId)) stateService.insert(id, userId);
        return new ResultBody<>(true, 200, courseContentDao.getById(id));
    }

    @Override
    public ResultBody<?> getState(int id) {
        return new ResultBody<>(true, 200, stateService.getState(id));
    }

    @Override
    public ResultBody<?> save(CourseContent courseContent) {
        courseContentDao.save(courseContent);
        return new ResultBody<>(true, 200, courseContent.getId());
    }

    @Override
    public ResultBody<?> update(CourseContent courseContent) {
        courseContentDao.update(courseContent);
        return new ResultBody<>(true, 200, null);
    }

    @Override
    public ResultBody<?> delete(int id) {
        courseContentDao.delete(id);
        return new ResultBody<>(true, 200, null);
    }

    @Override
    public List<CourseContent> getCourseContents(int courseId) {
        return courseContentDao.getCourseContents(courseId);
    }


}
