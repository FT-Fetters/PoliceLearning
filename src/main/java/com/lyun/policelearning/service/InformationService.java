package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface InformationService {
    List<JSONObject> findAll();
    JSONObject getInformationById(int id);
    PageResult findPage(PageRequest pageRequest);
    boolean insertInformation(Information information);
    boolean deleteById(int id);
    boolean updateById(Information information);
    boolean updateTopById(int id,int istop);
    void updateView(int id);
}
