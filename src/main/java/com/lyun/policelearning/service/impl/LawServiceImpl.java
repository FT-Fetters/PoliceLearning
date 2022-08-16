package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CollectDao;
import com.lyun.policelearning.dao.LawDao;
import com.lyun.policelearning.dao.LawTypeDao;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.LawType;
import com.lyun.policelearning.service.LawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LawServiceImpl implements LawService {
    @Autowired
    LawDao lawDao;
    @Autowired
    LawTypeDao lawTypeDao;
    @Autowired
    CollectDao collectDao;

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
        List<JSONObject> lawtypes = new ArrayList<>();
        for(LawType lawtype : lawTypes){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",lawtype.getId());
            jsonObject.put("lawtype",lawtype.getLawtype());
            lawtypes.add(jsonObject);
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
        if(keywords != null){
            String keyword = keywords.toJSONString();
            for(LawType lawType : lawTypeDao.findTitleByName(lawtype)){
                JSONArray jsonArray = JSONArray.parseArray(lawType.getTitle());
                if(jsonArray != null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",title);
                    jsonArray.add(jsonObject);
                    String str = jsonArray.toString();
                    lawTypeDao.updateTitleByName(str,lawtype);
                }else {
                    JSONArray jsonArray1 = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",title);
                    jsonArray1.add(jsonObject);
                    String str = jsonArray1.toString();
                    lawTypeDao.updateTitleByName(str,lawtype);
                }
            }
            lawDao.insert(lawtype,title,content,explaination,crime,keyword);
        }else {
            for(LawType lawType : lawTypeDao.findTitleByName(lawtype)){
                JSONArray jsonArray = JSONArray.parseArray(lawType.getTitle());
                if(jsonArray != null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",title);
                    jsonArray.add(jsonObject);
                    String str = jsonArray.toString();
                    lawTypeDao.updateTitleByName(str,lawtype);
                }else {
                    JSONArray jsonArray1 = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",title);
                    jsonArray1.add(jsonObject);
                    String str = jsonArray1.toString();
                    lawTypeDao.updateTitleByName(str,lawtype);
                }
            }
            lawDao.insert(lawtype,title,content,explaination,crime,null);
        }
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        if(id <= 0){
            return false;
        }
        collectDao.deleteById(3,id);
        for (LawType lawType : lawTypeDao.findTitleByName(lawDao.findLawById(id).getLawtype())){
            if (lawTypeDao.getTitleById(lawType.getId()) != null){
                JSONArray jsonArray =  JSONArray.parseArray(lawTypeDao.getTitleById(lawType.getId()).getTitle());
                if (jsonArray != null){
                    JSONObject del = new JSONObject();
                    if (lawDao.findLawById(id).getTitle() != null) {
                        del.put("name", lawDao.findLawById(id).getTitle());
                        jsonArray.remove(del);
                        lawTypeDao.updateTitleByName(jsonArray.toJSONString(), lawType.getLawtype());
                    }
                }
            }
        }
        lawDao.deleteById(id);
        return true;
    }

    @Override
    public boolean updateById(int id, String title,String lawtype, String content, String explaination, String crime, JSONArray keyword) {
        //对比法律类型是否存在变化 如果变了 则需要删除原来lawtype中的title 并且新增当前lawtype的title 若没变则更新原来lawtype中的title
        String newlawtype = lawtype;
        String oldlawtype = lawDao.findLawById(id).getLawtype();
        String newTitle = title;
        String oldTitle = lawDao.findLawById(id).getTitle();
        if (newlawtype.equals(oldlawtype)){
            for(LawType lawType : lawTypeDao.findTitleByName(oldlawtype)){
                JSONArray jsonArray = JSONArray.parseArray(lawType.getTitle());
                JSONObject del = new JSONObject();
                del.put("name",oldTitle);
                jsonArray.remove(del);
                JSONObject ins = new JSONObject();
                ins.put("name",newTitle);
                jsonArray.add(ins);
                String str = jsonArray.toJSONString();
                lawTypeDao.updateTitleByName(str,oldlawtype);
            }
        }else {
            for(LawType lawType : lawTypeDao.findTitleByName(oldlawtype)){
                JSONArray jsonArray = JSONArray.parseArray(lawType.getTitle());
                JSONObject del = new JSONObject();
                del.put("name",oldTitle);
                jsonArray.remove(del);
                String str = jsonArray.toJSONString();
                lawTypeDao.updateTitleByName(str,oldlawtype);
            }
            for (LawType lawType : lawTypeDao.findTitleByName(newlawtype)){
                JSONArray jsonArray = JSONArray.parseArray(lawType.getTitle());
                if(jsonArray != null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",newTitle);
                    jsonArray.add(jsonObject);
                    String str = jsonArray.toString();
                    lawTypeDao.updateTitleByName(str,lawtype);
                }else {
                    JSONArray jsonArray1 = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",newTitle);
                    jsonArray1.add(jsonObject);
                    String str = jsonArray1.toString();
                    lawTypeDao.updateTitleByName(str,lawtype);
                }
            }
        }
        if (keyword != null){
            String keywords = keyword.toJSONString();
            lawDao.updateById(id,title,lawtype,content,explaination,crime,keywords);
        }else {
            lawDao.updateById(id,title,lawtype,content,explaination,crime,null);
        }
        return true;
    }

    @Override
    public List<JSONObject> findTitleByNameForManage(String title) {
        List<JSONObject> res = new ArrayList<>();
        List<Law> laws = lawDao.findLawByTitle(title);
        for(Law law : laws){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",law.getId());
            jsonObject.put("title",law.getTitle());
            res.add(jsonObject);
        }
        return res;
    }

    @Override
    public int count() {
        return lawDao.count();
    }

    @Override
    public void insertType(String lawtype) {
        lawTypeDao.insertType(lawtype);
    }

    @Override
    public void deleteType(int id) {
        LawType lawType = lawTypeDao.getTitleById(id);
        //JSONArray title = JSONArray.parseArray(lawType.getTitle());
        lawDao.deleteByType(lawType.getLawtype());
        /*if (title != null){
            for(Object o : title){
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.getString("name") != null){
                    lawDao.deleteByType(jsonObject.getString("name"));
                }
            }
        }*/
        lawTypeDao.deleteType(id);
    }

    @Override
    public void updateType(int id, String type) {
        String lawtype = lawTypeDao.getTitleById(id).getLawtype();
        lawDao.updateType(lawtype,type);
        lawTypeDao.updateType(id, type);
    }


    private JSONObject lawToJson(Law law) {
        JSONObject res = new JSONObject();
        /*String content = changeToHtml(law.getContent());
        if(!content.contains("<br><br>")){
            content = content.replace("<br>","<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp");
            content = "&nbsp&nbsp&nbsp&nbsp&nbsp"+content;
        }else {
            content = content.replace("<br><br>","<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp");
            content = "&nbsp&nbsp&nbsp&nbsp&nbsp"+content;
        }*/
        String explaination = changeToHtml(law.getExplaination());
        String crime = changeToHtml(law.getCrime());
        res.put("id",law.getId());
        res.put("lawtype",law.getLawtype());
        res.put("title",law.getTitle());
        res.put("conten",law.getContent());
        res.put("explaination",explaination);
        res.put("crime",crime);
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
    /**
     * 将数据库中需要分段的内容进行修改
     * @param str
     * @return 返回新的文本
     */
    private String changeToHtml(String str){
        if (str != null){
            str = str.replace("\n","<br>");
        }
        return str;
    }
}
