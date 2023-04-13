package com.lyun.policelearning.service;

import com.lyun.policelearning.entity.Tool;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface ToolService {
    PageResult getAll(PageRequest pageRequest,int type);
    PageResult findByTitle(PageRequest pageRequest,String title);
    Tool getById(int id);
    void insert(Tool tool);
    void update(Tool tool);
    void delete(int id);
    Object list(int type,String title);
}
