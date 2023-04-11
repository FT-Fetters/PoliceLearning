package com.lyun.policelearning.service;

import com.lyun.policelearning.utils.ResultBody;

public interface CourseUsrLearnService {

    ResultBody<?> submitLearnTime(long learnTime,long courseId, long contentId, long userId);

    ResultBody<?> checkFinished(long courseId, long contentId, long userId);



}
