package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.LawDao;
import com.lyun.policelearning.dao.LawTypeDao;
import com.lyun.policelearning.entity.Course;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.LawType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LawServiceImpl implements LawService{
    @Autowired
    LawDao lawDao;
    @Autowired
    LawTypeDao lawTypeDao;


    //能获取到json格式的title目录
    @Override
    public List<JSONObject> findAll() {
        List<JSONObject> lawtype = new ArrayList<>();
        List<LawType> lawTypes = lawTypeDao.findAll();
        for(LawType lawType:lawTypes){
            JSONObject res = this.lawTypeToJson(lawType);
            lawtype.add(res);
        }
        return lawtype;
    }

    @Override
    public JSONObject findAllType() {
        JSONObject lawType = new JSONObject();
        List<LawType> lawTypes  = lawTypeDao.findAll();
        List<String> lawtypes = new ArrayList<>();
        for(LawType lawtype : lawTypes){
            lawtypes.add(lawtype.getLawtype());
        }
        lawType.put("lawTypes",lawtypes);
        return lawType;
    }

    @Override
    public JSONObject findTitleByName(String name) {
        JSONObject catalogue = new JSONObject();
        List<LawType> lawTypes = lawTypeDao.findTitleByName(name);
        for(LawType lawType : lawTypes){
            catalogue.put("title",JSONArray.parseArray(lawType.getTitle()));
        }
        return catalogue;
    }

    @Override
    public JSONObject findContent(String title) {
        JSONObject body = new JSONObject();
        List<Law> laws = lawDao.findContent(title);
        for(Law law:laws){
            body = lawToJson(law);
        }
        return body;
    }

    @Override
    public JSONObject findContentById(int id) {
        JSONObject body = new JSONObject();
        List<Law> laws = lawDao.findContentById(id);
        for(Law law:laws){
            body = lawToJson(law);
        }
        return body;
    }

    /**
     * 将新的json追加到原有json的后面
     * @param name
     * @param explain
     * @param id
     * @return 如果更新成功则返回true
     */
    @Override
    public boolean updateKeyword(String name, String explain,int id) {
       JSONArray keywords = getKeywordById(id);
       JSONObject keyword = new JSONObject();
       keyword.put(name,explain);
       keywords.add(keyword);
       //调用更新数据库的方法
       String str = keywords.toJSONString();
       lawDao.updateKeyword(id,str);
        return true;
    }

    @Override
    public boolean insert(String lawtype, String title, String content, String explaination, String crime, JSONArray keywords) {
        String keyword = keywords.toJSONString();
        lawDao.insert(lawtype,title,content,explaination,crime,keyword);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        if(id <= 0){
            return false;
        }
        lawDao.deleteById(id);
        return true;
    }

    @Override
    public boolean updateById(int id, String title,String lawtype, String content, String explaination, String crime, JSONArray keyword) {
        String keywords = keyword.toJSONString();
        lawDao.updateById(id,lawtype,title,content,explaination,crime,keywords);
        return true;
    }


    private JSONObject lawToJson(Law law) {
        JSONObject res = new JSONObject();
        res.put("id",law.getId());
        res.put("lawtype",law.getLawtype());
        res.put("title",law.getTitle());
        res.put("conten",law.getContent());
        res.put("explaination",law.getExplaination());
        res.put("crime",law.getCrime());
        JSONArray keyWord = JSONArray.parseArray(law.getKeyword());
        res.put("keyWord",keyWord);
        return res;
    }
    private JSONObject lawTypeToJson(LawType lawType) {
        JSONObject res = new JSONObject();
        res.put("id",lawType.getId());
        res.put("lawtype",lawType.getLawtype());
        JSONArray title = JSONArray.parseArray(lawType.getTitle());
        res.put("title",title);
        return res;
    }
    private JSONArray getKeywordById(int id){
        Law law = lawDao.findLawById(id);
        JSONArray keywords = JSONArray.parseArray(law.getKeyword());
        return keywords;
    }
}
