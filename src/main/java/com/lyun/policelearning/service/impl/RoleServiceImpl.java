package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.RoleDao;
import com.lyun.policelearning.entity.Role;
import com.lyun.policelearning.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public void newRole(String name, boolean admin) {
      roleDao.newRole(name,admin);
    }

    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }

    @Override
    public void deleteRole(int id) {
        roleDao.deleteRole(id);
    }

    @Override
    public void updateRole(String name, boolean admin,Integer id) {
        roleDao.updateRole(name,admin,id);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public void updateAdmin(int id, boolean admin) {
        roleDao.updateAdmin(id, admin);
    }
}
