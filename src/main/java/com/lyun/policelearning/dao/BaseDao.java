package com.lyun.policelearning.dao;

import java.util.List;

public interface BaseDao<T> {
    List<T> findAll();
}