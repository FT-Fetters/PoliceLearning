package com.lyun.policelearning.service;

import com.lyun.policelearning.dao.RoleDao;
import com.lyun.policelearning.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleDao roleDao;

    @Override
    public void newRole(String name, Integer power) {
      roleDao.newRole(name,power);
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
    public void updateRole(String name, Integer power,Integer id) {
        roleDao.updateRole(name,power,id);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
