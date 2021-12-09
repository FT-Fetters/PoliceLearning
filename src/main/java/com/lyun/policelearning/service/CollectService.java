package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.Collect;

import java.sql.Date;
import java.util.List;

public interface CollectService {
    List<Collect> findAll();
    void collect(int type, int articleId,int userId);
}
