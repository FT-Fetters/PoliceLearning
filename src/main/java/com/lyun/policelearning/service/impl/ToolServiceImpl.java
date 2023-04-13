package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.controller.tool.ToolManageApi;
import com.lyun.policelearning.dao.ToolDao;
import com.lyun.policelearning.dao.ToolTypeDao;
import com.lyun.policelearning.entity.Tool;
import com.lyun.policelearning.service.ToolService;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolServiceImpl implements ToolService {
    public static Page page;

    @Autowired
    ToolDao toolDao;

    @Autowired
    ToolTypeDao toolTypeDao;

    @Override
    public PageResult getAll(PageRequest pageRequest, int type) {
        return PageUtil.getPageResult(getPageInfo(pageRequest,type),page);
    }

    @Override
    public PageResult findByTitle(PageRequest pageRequest, String title) {
        return PageUtil.getPageResult(getPageInfo1(pageRequest,title),page);
    }

    @Override
    public Tool getById(int id) {
        Tool tool = toolDao.getById(id);
        tool.setTypeName(toolTypeDao.getById(tool.getType()).getName());
        return tool;
    }

    @Override
    public void insert(Tool tool) {
        toolDao.insert(tool);
    }

    @Override
    public void update(Tool tool) {
        toolDao.update(tool);
    }

    @Override
    public void delete(int id) {
        toolDao.delete(id);
    }

    @Override
    public Object list(int type, String title) {
        List<JSONObject> res = new ArrayList<>();
        for(Tool tool : toolDao.list(type,title)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",tool.getId());
            jsonObject.put("title",tool.getTitle());
            jsonObject.put("date",tool.getDate());
            res.add(jsonObject);
        }
        return res;
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest,int type) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Tool tool: toolDao.all(type)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",tool.getId());
            jsonObject.put("title",tool.getTitle());
            jsonObject.put("date",tool.getDate());
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }


    private PageInfo<?> getPageInfo1(PageRequest pageRequest,String title) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Tool tool: toolDao.findByTitle(title)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",tool.getId());
            jsonObject.put("title",tool.getTitle());
            jsonObject.put("date",tool.getDate());
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }
}
