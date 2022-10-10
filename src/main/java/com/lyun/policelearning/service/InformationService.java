package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;

import java.util.List;

public interface InformationService {
    List<JSONObject> findAll() throws Exception;
    JSONObject getInformationById(int id) throws Exception;
    PageResult findPage(PageRequest pageRequest);
    boolean insertInformation(Information information);
    boolean deleteById(int id);
    void updateById(Information information);
    boolean updateTopById(int id,int istop);
    void updateView(int id);
    String getPictureById(int id);
    List<JSONObject> getPicture() throws Exception;
    List<JSONObject> getAllPicture();
    void updateChoose();
    void setChangePicture(List<Integer> ids);
    void updatePicture(int id,String picture);
    void deletePicture(int id);
    Information getInformationByTitle(String title);
    int count();
    PageResult findPicture(PageRequest pageRequest);
    List<JSONObject> findPicture();
    PageResult search(PageRequest pageRequest,String word);


}
