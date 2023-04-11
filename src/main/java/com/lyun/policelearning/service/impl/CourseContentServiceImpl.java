package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CourseContentDao;
import com.lyun.policelearning.dao.CourseUsrLearnDao;
import com.lyun.policelearning.entity.CourseContent;
import com.lyun.policelearning.entity.CourseUsrLearn;
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

    @Autowired
    private CourseUsrLearnDao courseUsrLearnDao;

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

    @Override
    public ResultBody<?> getCourseFinishState(long courseId) {
        List<CourseContent> courseContents = courseContentDao.getCourseContents(courseId);
        JSONArray res = new JSONArray();
        for (CourseContent courseContent : courseContents) {
            int contentId = courseContent.getId();
            List<CourseUsrLearn> courseUsrLearns = courseUsrLearnDao.queryByCourseAndContent(courseId, contentId);
            JSONObject state = new JSONObject();
            state.put("courseId", contentId);
            state.put("contentId", contentId);
            state.put("planTime", courseContent.getPlanTime());
            state.put("state", courseUsrLearns);
            res.add(state);
        }
        return new ResultBody<>(true, 200, res);
    }


}
