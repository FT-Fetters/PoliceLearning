package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.dao.RuleDao;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RuleServiceImpl implements RuleService{
    @Autowired
    RuleDao ruleDao;

    @Override
    public List<JSONObject> findAll() {
        List<JSONObject> rules = new ArrayList<>();
        List<Rule> list = new ArrayList<>();
        for(Rule rule: ruleDao.findAll()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",rule.getId());
            jsonObject.put("title",rule.getTitle());
            jsonObject.put("view",rule.getView());
            jsonObject.put("date",rule.getDate());
            rules.add(jsonObject);
        }
        return rules;
    }

    @Override
    public JSONObject getRuleById(int id) {
        JSONObject rule = new JSONObject();
        rule.put("title",ruleDao.getRuleById(id).getTitle());
        rule.put("content",ruleDao.getRuleById(id).getContent());
        rule.put("date",ruleDao.getRuleById(id).getDate());
        rule.put("view",ruleDao.getRuleById(id).getView());
        return rule;
    }

    /**  根据pageRequest中的当前页、每页的大小，返回自定义page类型的队形
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult findPage(PageRequest pageRequest) {
        //将pageInfo传递到getPageResult中获得result结果，而pageInfo又是与数据库中的数据有关的
        return PageUtil.getPageResult(getPageInfo(pageRequest));
    }

    @Override
    public boolean insertRule(Rule rule) {
        if (rule == null){
            return false;
        }
        ruleDao.insertRule(rule);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        if(id <= 0){
            return false;
        }else {
            ruleDao.deleteById(id);
            return true;
        }
    }

    @Override
    public boolean updateById( Rule rule) {
        if(rule == null){
            return false;
        }else {
            ruleDao.updateById(rule);
            return true;
        }
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        PageHelper.startPage(pageNum,pageSize);
        List<Rule> rules = ruleDao.selectPage();
        return new PageInfo<>(rules);
    }
}
