package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.Role;

import java.util.List;

public interface RoleService {

    void newRole(String name,Integer power);
    Role findById(int id);
    void deleteRole(int id);
    void updateRole(String name,Integer power,Integer id);
    List<Role> findAll();

}
