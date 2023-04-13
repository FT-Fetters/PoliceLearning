package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.course.CourseQuestionDao;
import com.lyun.policelearning.dao.course.CourseQuestionStateDao;
import com.lyun.policelearning.entity.course.CourseQuestion;
import com.lyun.policelearning.entity.course.CourseQuestionState;
import com.lyun.policelearning.service.CourseQuestionService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseQuestionServiceImpl implements CourseQuestionService {

    @Autowired
    private CourseQuestionDao courseQuestionDao;

    @Autowired
    private CourseQuestionStateDao courseQuestionStateDao;

    @Override
    public ResultBody<?> getCourseQuestion(int contentId) {
        List<CourseQuestion> courseQuestions = courseQuestionDao.queryByContentId(contentId);
        for (CourseQuestion courseQuestion : courseQuestions) {
            courseQuestion.setAnswer(null);
        }
        return new ResultBody<>(true, 200, courseQuestions);
    }

    @Override
    public ResultBody<?> insert(CourseQuestion courseQuestion) {
        courseQuestionDao.insert(courseQuestion);
        return new ResultBody<>(true, 200, courseQuestion.getId());
    }

    @Override
    public ResultBody<?> delete(int id) {
        courseQuestionDao.delete(id);
        return new ResultBody<>(true, 200, id);
    }

    @Override
    public ResultBody<?> update(CourseQuestion courseQuestion) {
        courseQuestionDao.update(courseQuestion);
        return new ResultBody<>(true, 200, courseQuestion.getId());
    }

    @Override
    public ResultBody<?> submit(List<Boolean> inputs, int contentId, int userId) {
        StringBuilder inputStr = new StringBuilder();
        for (Boolean input : inputs) {
            inputStr.append(input ? "1" : "0");
        }
        CourseQuestionState courseQuestionState = CourseQuestionState.builder()
                .userId(userId).contentId(contentId)
                .userInput(inputStr.toString())
                .build();
        courseQuestionStateDao.submit(courseQuestionState);
        List<Boolean> res = new ArrayList<>();
        List<CourseQuestion> courseQuestions = courseQuestionDao.queryByContentId(contentId);
        for (int i = 0; i < courseQuestions.size(); i++) {
                res.add(courseQuestions.get(i).getAnswer() == inputs.get(i));
        }
        return new ResultBody<>(true,200,res);
    }

    @Override
    public ResultBody<?> checkFinished(int contentId, int userId) {
        CourseQuestionState courseQuestionState = courseQuestionStateDao.queryByContentAndUser(contentId, userId);
        if (courseQuestionState == null){
            return new ResultBody<>(true,200,false);
        }else {
            if (courseQuestionState.getUserInput() != null && !courseQuestionState.getUserInput().equals(""))
                return new ResultBody<>(true,200,true);
            else
                return new ResultBody<>(true,200,false);
        }
    }
}
