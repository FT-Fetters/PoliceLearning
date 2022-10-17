package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.StateDao;
import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.State;
import com.lyun.policelearning.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StateServiceImpl implements StateService {
    @Autowired
    StateDao stateDao;
    @Autowired
    UserDao userDao;

    @Override
    public boolean check(int tid, int uid) {
        if (stateDao.check(tid, uid) != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void insert(int tid, int uid) {
        stateDao.insert(tid, uid);
    }

    @Override
    public List<JSONObject> getState(int tid) {
        List<JSONObject> res = new ArrayList<>();
        for (State state : stateDao.getState(tid)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",state.getId());
            jsonObject.put("username",userDao.getById(state.getUid()).getUsername());
            jsonObject.put("isView",state.isView());
            res.add(jsonObject);
        }
        return res;
    }
}
