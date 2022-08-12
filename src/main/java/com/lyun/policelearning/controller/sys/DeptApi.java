package com.lyun.policelearning.controller.sys;

import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.entity.Dept;
import com.lyun.policelearning.service.DeptService;
import com.lyun.policelearning.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptApi {

    @Autowired
    private DeptService deptService;

    @Permission()
    @PostMapping("/list")
    public Object list(@RequestBody Dept dept){
        List<Dept> list = deptService.selectDeptList(dept);
        return new ResultBody<>(true,200,list);
    }

    @Permission()
    @GetMapping(value = "/{id}")
    public Object getInfo(@PathVariable("id") String id){
        return new ResultBody<>(true,200,deptService.selectDeptById(id));
    }

    @Permission()
    @PostMapping
    public Object add(@RequestBody Dept dept){
        return new ResultBody<>(true,200,deptService.insertDept(dept));
    }

    @Permission()
    @PutMapping
    public Object edit(@RequestBody Dept dept)
    {
        return new ResultBody<>(true,200,deptService.updateDept(dept));
    }

    @Permission()
    @DeleteMapping("/{ids}")
    public Object remove(@PathVariable String[] ids)
    {
        return new ResultBody<>(true,200,deptService.deleteDeptByIds(ids));
    }


}
