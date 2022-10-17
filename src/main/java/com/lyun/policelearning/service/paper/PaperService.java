package com.lyun.policelearning.service.paper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.entity.Paper;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface PaperService {

    PageResult selectAll(PageRequest pageRequest);
    JSONArray userSelectAll(int userId);
    int generate(int j,int m,int s,String title,int uid);
    JSONObject getById(int id);
    JSONObject userGetById(int id);
    boolean delete(int id);
    boolean enable(int id);
    boolean disable(int id);
    String getPaperAnswer(int id);
    int count();
    List<JSONObject> getHistory(int uid);
    void paperAddQue(Integer paperId, String type, Integer queId);
    void paperDelQue(Integer paperId, String type, Integer index);
}
