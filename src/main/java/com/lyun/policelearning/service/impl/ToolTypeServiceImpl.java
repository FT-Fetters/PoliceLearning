package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.ToolTypeDao;
import com.lyun.policelearning.entity.ToolType;
import com.lyun.policelearning.service.ToolService;
import com.lyun.policelearning.service.ToolTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolTypeServiceImpl implements ToolTypeService {
    @Autowired
    ToolTypeDao toolTypeDao;


    @Override
    public Object all() {
        List<ToolType> res = getTree(0);
        return res;
    }

    @Override
    public void delete(int id) {
        deleteTree(id);
        toolTypeDao.delete(id);
    }

    @Override
    public void insert(String name,Integer pid) {
        toolTypeDao.insert(name,pid);
    }

    private List<ToolType> getTree(int pid) {
        List<ToolType> res = new ArrayList<>();
        //List<SysOrg> list = unitDao.findByFid(fid);
        for(ToolType toolType : toolTypeDao.getByPid(pid)){
            List<ToolType> news = getTree(toolType.getId());
            if(news == null || news.size() == 0){
                res.add(toolType);
                continue;
            }else {
                toolType.setChildren(news);
                res.add(toolType);
            }

        }
        return res;
    }

    private void deleteTree(int pid){
        List<ToolType> toolTypes = toolTypeDao.getByPid(pid);
        if (toolTypes == null || toolTypes.size()==0){
            toolTypeDao.delete(pid);
            return;
        }else {
            for(ToolType toolType : toolTypes){
                deleteTree(toolType.getId());
                toolTypeDao.delete(toolType.getId());
            }
        }
    }
}
