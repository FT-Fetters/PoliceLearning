package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.dao.InformationDao;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.utils.ImageTools;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import com.lyun.policelearning.utils.StringFileter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InformationServiceImpl implements InformationService{

    @Autowired
    private InformationDao informationDao;

    @Override
    public List<JSONObject> findAll() throws Exception {
        List<JSONObject> informationList = new ArrayList<>();
        //先将istop为true的数据放进去
        for(Information information : informationDao.findTop()){
            JSONObject jsonObject = new JSONObject();
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
            informationList.add(jsonObject);
        }
        for(Information information : informationDao.findNotTop()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            if(information.getPicture() != null){
                String savePath = PathTools.getRunPath()+"/image/";
                String imagePath = savePath + information.getPicture();
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                jsonObject.put("picture", imgBase64);
            }
            informationList.add(jsonObject);
        }
        return informationList;
    }

    @Override
    public JSONObject getInformationById(int id) throws Exception {
        JSONObject information = new JSONObject();
        String content = informationDao.getInformationById(id).getContent();
        information.put("id",id);
        information.put("title",informationDao.getInformationById(id).getTitle());
        information.put("content",content);
        information.put("date",informationDao.getInformationById(id).getDate());
        information.put("view",informationDao.getInformationById(id).getView());
        if(informationDao.getInformationById(id).getPicture() != null){
            String savePath = PathTools.getRunPath()+"/image/";
            String imagePath = savePath + informationDao.getInformationById(id).getPicture();
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            String imgBase64 = ImageTools.imgToBase64(bufferedImage);
            information.put("picture", imgBase64);
        }
        return information;
    }

    /**  根据pageRequest中的当前页、每页的大小，返回自定义page类型的队形
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult findPage(PageRequest pageRequest) {
        //将pageInfo传递到getPageResult中获得result结果，而pageInfo又是与数据库中的数据有关的
        return PageUtil.getPageResult(getPageInfo(pageRequest));
    }

    @Override
    public boolean insertInformation(Information information) {
        if (information == null){
            return false;
        }
        informationDao.insertInformation(information);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        if(id <= 0){
            return false;
        }else {
            informationDao.deleteById(id);
            return true;
        }
    }

    @Override
    public void updateById( Information information) {
            informationDao.updateById(information);
    }

    @Override
    public boolean updateTopById(int id, int istop) {
       informationDao.updateTopById(id,istop);
       return true;
    }

    @Override
    public void updateView(int id) {
        informationDao.updateView(id);
    }

    @Override
    public String getPictureById(int id) {
        return informationDao.getPictureById(id).getPicture();
    }

    @Override
    public List<JSONObject> getPicture() throws Exception {
        List<JSONObject> res = new ArrayList<>();
        for(Information information : informationDao.getPicture()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            //返回base64格式的图片
            if(information.getPicture() != null){
                String savePath = PathTools.getRunPath()+"/image/";
                String imagePath = savePath + information.getPicture();
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                jsonObject.put("picture",imgBase64);
            }else {
                jsonObject.put("picture",null);
            }
            jsonObject.put("ischoose",information.getIschoose());
            res.add(jsonObject);
        }
        return res;
    }

    @SneakyThrows
    @Override
    public List<JSONObject> getAllPicture()  {
        List<JSONObject> res = new ArrayList<>();
        for(Information information : informationDao.getAllPicture()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            if(information.getPicture() != null){
                String savePath = PathTools.getRunPath()+"/image/";
                String imagePath = savePath + information.getPicture();
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                jsonObject.put("picture",imgBase64);
            }else {
                jsonObject.put("picture",null);
            }
            jsonObject.put("ischoose",information.getIschoose());
            res.add(jsonObject);
        }
        return res;
    }

    @Override
    public void updateChoose() {
        informationDao.updateChoose();
    }

    @Override
    public void setChangePicture(List<Integer> ids) {
        for(Integer id : ids){
            informationDao.setChangePicture(id);
        }
    }

    @Override
    public void updatePicture(int id,String picture) {
        informationDao.updatePicture(id,picture);
    }

    @Override
    public void deletePicture(int id) {
        informationDao.deletePicture(id);
    }

    @Override
    public Information getInformationByTitle(String title) {
        return informationDao.getInformationByTitle(title);
    }

    @Override
    public int count() {
        return informationDao.count();
    }


    @SneakyThrows
    private PageInfo<?> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Information information : informationDao.selectPage()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            if(information.getPicture() != null){
                String savePath = PathTools.getRunPath()+"/image/";
                String imagePath = savePath + information.getPicture();
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                jsonObject.put("picture", imgBase64);
            }
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }

}
