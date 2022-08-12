package com.lyun.policelearning.dao;


import com.lyun.policelearning.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptDao {
    /**
     * 查询部门
     *
     * @param id 部门主键
     * @return 部门
     */
    public Dept selectDeptById(String id);

    /**
     * 查询部门列表
     *
     * @param dept 部门
     * @return 部门集合
     */
    public List<Dept> selectDeptList(Dept dept);

    /**
     * 新增部门
     *
     * @param dept 部门
     * @return 结果
     */
    public int insertDept(Dept dept);

    /**
     * 修改部门
     *
     * @param dept 部门
     * @return 结果
     */
    public int updateDept(Dept dept);

    /**
     * 删除部门
     *
     * @param id 部门主键
     * @return 结果
     */
    public int deleteDeptById(String id);

    /**
     * 批量删除部门
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeptByIds(String[] ids);
}
