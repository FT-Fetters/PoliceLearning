package com.lyun.policelearning.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.CollectDao;
import com.lyun.policelearning.entity.Collect;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.service.CollectService;
import com.lyun.policelearning.utils.ImageTools;
import com.lyun.policelearning.utils.PathTools;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;

    @Override
    public List<Collect> findAll() {
        return collectDao.findAll();
    }

    @Override
    public boolean collect(int type, int articleId,int userId) {
        if(collectDao.check(userId,type,articleId) != null){
            return false;
        }
        Date date = new Date(System.currentTimeMillis());
        collectDao.collect(type,articleId,userId,date);
        return true;
    }

    @Override
    public Object findCollect(int type,int userId) {
        if(type == 1){
            return findInformation(userId);
        }
        else if(type == 2) {
            return findLaw(userId);
        }else {
            return findRule(userId);
        }
    }

    @Override
    public void deleteCollect(int type, int articleId, int userId) {
        collectDao.deleteCollect(type,articleId,userId);
    }

    @Override
    public boolean isCollect(int type, int articleId, int userId) {
        return collectDao.check(userId, type, articleId) != null;
    }

    @SneakyThrows
    public List<JSONObject> findInformation(int userId){
        List<JSONObject> res = new ArrayList<>();
        for(Integer id : collectDao.findInformation(userId)){
            JSONObject jsonObject = new JSONObject();
            Information information = collectDao.getInformation(id);
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            if(information.getPicture() != null) {
                String savePath = PathTools.getRunPath()+"/image/";
                String imagePath = savePath + information.getPicture();
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                jsonObject.put("picture", imgBase64);
            }
            res.add(jsonObject);
        }
        return res;
    }
    public List<JSONObject> findLaw(int userId){
        List<JSONObject> res = new ArrayList<>();
        for(Integer id : collectDao.findLaw(userId)){
            JSONObject jsonObject = new JSONObject();
            Law law = collectDao.getLaw(id);
            jsonObject.put("title",law.getTitle());
            res.add(jsonObject);
        }
        return res;
    }
    public List<JSONObject> findRule(int usrId){
        List<JSONObject> res = new ArrayList<>();
        for(Integer id : collectDao.findRule(usrId)){
            JSONObject jsonObject = new JSONObject();
            Rule rule = collectDao.getRule(id);
            jsonObject.put("id",rule.getId());
            jsonObject.put("title",rule.getTitle());
            jsonObject.put("view",rule.getView());
            jsonObject.put("date",rule.getDate());
            res.add(jsonObject);
        }
        return res;
    }
}
