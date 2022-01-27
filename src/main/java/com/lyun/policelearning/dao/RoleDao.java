package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao extends BaseDao<Role> {

    void newRole(String name,Integer power);
    Role findById(int id);
    void deleteRole(int id);
    void updateRole(String name,Integer power,Integer id);
}
