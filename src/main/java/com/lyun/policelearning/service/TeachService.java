package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.Teach;

public interface TeachService {
    Teach getById(int id);
    int save(String content);
    void update(int id,String content);
}
