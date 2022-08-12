package com.lyun.policelearning.service.impl;

import com.lyun.policelearning.dao.DeptDao;
import com.lyun.policelearning.entity.Dept;
import com.lyun.policelearning.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    /**
     * 查询部门
     *
     * @param id 部门主键
     * @return 部门
     */
    @Override
    public Dept selectDeptById(String id)
    {
        return deptDao.selectDeptById(id);
    }

    /**
     * 查询部门列表
     *
     * @param dept 部门
     * @return 部门
     */
    @Override
    public List<Dept> selectDeptList(Dept dept)
    {
        return deptDao.selectDeptList(dept);
    }

    /**
     * 新增部门
     *
     * @param dept 部门
     * @return 结果
     */
    @Override
    public int insertDept(Dept dept)
    {
        return deptDao.insertDept(dept);
    }

    /**
     * 修改部门
     *
     * @param dept 部门
     * @return 结果
     */
    @Override
    public int updateDept(Dept dept)
    {
        return deptDao.updateDept(dept);
    }

    /**
     * 批量删除部门
     *
     * @param ids 需要删除的部门主键
     * @return 结果
     */
    @Override
    public int deleteDeptByIds(String[] ids)
    {
        return deptDao.deleteDeptByIds(ids);
    }

    /**
     * 删除部门信息
     *
     * @param id 部门主键
     * @return 结果
     */
    @Override
    public int deleteDeptById(String id)
    {
        return deptDao.deleteDeptById(id);
    }
}
