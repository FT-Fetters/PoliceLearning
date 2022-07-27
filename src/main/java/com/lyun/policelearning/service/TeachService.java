package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.Teach;
import org.apache.ibatis.annotations.Param;

public interface TeachService {
    Teach getById(int id);
    int save( Teach teach);
    void update(int id,String content);
    void delete(int id);
}
