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

import java.util.ArrayList;
import java.util.List;
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
        List<Law> laws  = lawDao.findAll();;
        List<String> lawTypes = new ArrayList<>();
        for(Law law : laws){
            lawTypes.add(law.getLawtype());
        }
        lawType.put("lawTypes",lawTypes);
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


    private JSONObject lawToJson(Law law) {
        JSONObject res = new JSONObject();
        res.put("id",law.getId());
        res.put("lawtype",law.getLawtype());
        res.put("title",law.getTitle());
        res.put("conten",law.getContent());
        res.put("explaination",law.getExplaination());
        res.put("crime",law.getCrime());
        //用JSONArray去装目录，然后再将这个array作为一个整体放入json中
        //parseArray:将JSONObject转化成JSONArray
        //law.getKeyword()得到的不就是JSONArray吗？为什么还需要进行转化？
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
