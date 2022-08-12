package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao extends BaseDao<Role> {

    void newRole(String name,boolean admin);
    Role findById(int id);
    void deleteRole(int id);
    void updateRole(String name,boolean admin,Integer id);
    void updateAdmin(int id,boolean admin);
}
