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
            JSONObject tmp = new JSONObject();
            tmp.put("id" , course.getId());
            tmp.put("name",  course.getName());
            tmp.put("introduce",course.getIntroduce());
            JSONArray catalogue = JSONArray.parseArray(course.getCatalogue());
            tmp.put("catalogue",catalogue);
            courses.add(tmp);
        }
        return courses;
    }
}
