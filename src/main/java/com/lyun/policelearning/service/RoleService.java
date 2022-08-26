package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONArray;
import com.lyun.policelearning.entity.Role;

import java.util.List;

public interface RoleService {

    void newRole(String name,boolean admin);
    Role findById(int id);
    void deleteRole(int id);
    void updateRole(String name,boolean admin,Integer id);
    List<Role> findAll();
    void updateAdmin(int id,boolean admin);
    JSONArray roleDictionary();

}
