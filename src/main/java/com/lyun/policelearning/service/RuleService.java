package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface RuleService {
    List<JSONObject> findAll();
    JSONObject getRuleById(int id);
    PageResult findPage(PageRequest pageRequest);
    boolean insertRule(Rule rule);
    boolean deleteById(int id);
    boolean updateById(Rule rule);
    void updateView(int id);
    int count();
}
