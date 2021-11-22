package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CourseDao;
import com.lyun.policelearning.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseDao courseDao;

    @Override
    public List<JSONObject> findAll() {
        List<JSONObject> courses = new ArrayList<>();
        List<Course> courseList = courseDao.findAll();
        for (Course course : courseList) {
            JSONObject tmp = courseToJson(course);
            courses.add(tmp);
        }
        return courses;
    }

    @Override
    public JSONObject getCourseById(int id) {
        Course course = courseDao.getCourseById(id);
        return courseToJson(course);
    }

    private JSONObject courseToJson(Course course) {
        JSONObject res = new JSONObject();
        res.put("id",course.getId());
        res.put("name",course.getName());
        res.put("introduce",course.getIntroduce());
        res.put("type",course.getType());
        JSONArray catalogue = JSONArray.parseArray(course.getCatalogue());
        res.put("catalogue",catalogue);
        return res;
    }

    @Override
    public JSONArray getCatalogue(int id) {
        if (getCourseById(id) == null)
            return null;
        return JSONArray.parseArray(getCourseById(id).getString("catalogue"));
    }

    @Override
    public String getIntroduce(int id) {
        if (getCourseById(id) == null)
            return null;
        return getCourseById(id).getString("introduce");
    }
}
