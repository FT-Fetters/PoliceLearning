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
        if (course == null)return null;
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

    @Override
    public boolean changeType(int id, String type) {
        if (getCourseById(id) == null)return false;
        Course course = new Course();
        JSONObject courseJson = getCourseById(id);
        course.setId(courseJson.getInteger("id"));
        course.setType(type);
        course.setCatalogue(courseJson.getString("catalogue"));
        course.setName(courseJson.getString("name"));
        course.setIntroduce(courseJson.getString("introduce"));
        courseDao.update(course);
        return true;
    }

    @Override
    public JSONArray getByType(String type) {
        List<Course> courses = courseDao.getByType(type);
        JSONArray res = new JSONArray();
        for (Course course : courses) {
            res.add(courseToJson(course));
        }
        return res;
    }

    @Override
    public boolean changeIntroduce(int id,String introduce) {
        if (getCourseById(id) == null)return false;
        Course course = new Course();
        JSONObject courseJson = getCourseById(id);
        course.setId(courseJson.getInteger("id"));
        course.setType(courseJson.getString("type"));
        course.setCatalogue(courseJson.getString("catalogue"));
        course.setName(courseJson.getString("name"));
        course.setIntroduce(introduce);
        courseDao.update(course);
        return false;
    }
}
