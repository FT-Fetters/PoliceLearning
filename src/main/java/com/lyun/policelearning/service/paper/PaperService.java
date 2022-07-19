package com.lyun.policelearning.service.paper;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Paper;

import java.util.List;

public interface PaperService {

    List<Paper> selectAll();
    List<Paper> userSelectAll();
    int generate(int j,int m,int s,String title);
    JSONObject getById(int id);
    JSONObject userGetById(int id);
    boolean delete(int id);
    boolean enable(int id);
    boolean disable(int id);
}
