package com.lyun.policelearning.service;

public interface ToolTypeService {
    Object all();
    void delete(int id);
    void insert(String name,Integer pid);
    void update(String name,Integer id);
}
