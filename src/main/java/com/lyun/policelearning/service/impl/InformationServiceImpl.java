package com.lyun.policelearning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.dao.CollectDao;
import com.lyun.policelearning.dao.CommentDao;
import com.lyun.policelearning.dao.InformationDao;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.utils.ImageTools;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationDao informationDao;

    @Autowired
    private CollectDao collectDao;

    @Autowired
    private CommentDao commentDao;

    public static Page page;

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
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");

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
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                System.out.println(imagePath);
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");
            }
            informationList.add(jsonObject);
        }
        return informationList;
    }

    @Override
    public JSONObject getInformationById(int id) throws Exception {
        JSONObject information = new JSONObject();
        Information inf = informationDao.getInformationById(id);
        String content = inf.getContent();
        information.put("id",id);
        information.put("title",inf.getTitle());
        information.put("content",content);
        information.put("date",inf.getDate());
        information.put("view",inf.getView());
        if(inf.getPicture() != null){
            String savePath = PathTools.getRunPath()+"image/";
            String imagePath = savePath + inf.getPicture();
            File imageFile = new File(imagePath);
            String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
            if (imageFile.exists()){
                if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                    //jpg
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                    information.put("picture", imgBase64);
                }else {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    BufferedImage newBufferedImage = new BufferedImage(
                            bufferedImage.getWidth(), bufferedImage.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                            Color.WHITE, null);
                    String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                    information.put("picture", imgBase64);
                }

            }else {
                information.put("picture", "");
            }
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
        return PageUtil.getPageResult(getPageInfo(pageRequest),page);
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
            commentDao.deleteCommentByTypeAndId("inf",id);
            collectDao.deleteById(1,id);
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
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                System.out.println(imagePath);
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");
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
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                System.out.println(imagePath);
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");
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

    @Override
    public PageResult findPicture(PageRequest pageRequest) {
        return PageUtil.getPageResult(getPictureInfo(pageRequest),page);
    }

    @SneakyThrows
    @Override
    public List<JSONObject> findPicture() {
        List<JSONObject> res = new ArrayList<>();
        for (Information information : informationDao.getAllPicture()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", information.getId());
            jsonObject.put("title", information.getTitle());
            if (information.getPicture() != null) {
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                System.out.println(imagePath);
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");
            } else {
                jsonObject.put("picture", null);
            }
            jsonObject.put("ischoose", information.getIschoose());
            res.add(jsonObject);
        }
        return res;
    }

    @Override
    public PageResult search(PageRequest pageRequest, String word) {
        return  PageUtil.getPageResult(getPageSearch(pageRequest,word),page);

    }


    @SneakyThrows
    private PageInfo<?> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Information information : informationDao.selectPage()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            if(information.getPicture() != null){
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");
            }
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }
    @SneakyThrows
    private PageInfo<?> getPageSearch(PageRequest pageRequest,String word) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum,pageSize);
        List<JSONObject> res = new ArrayList<>();
        for(Information information : informationDao.search(word)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            if(information.getPicture() != null){
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                System.out.println(imagePath);
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());
                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else
                    jsonObject.put("picture","");
            }
            res.add(jsonObject);
        }
        return new PageInfo<>(res);
    }

    @SneakyThrows
    private PageInfo<?> getPictureInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        page = PageHelper.startPage(pageNum, pageSize);
        List<JSONObject> res = new ArrayList<>();
        for (Information information : informationDao.getAllPicture()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", information.getId());
            jsonObject.put("title", information.getTitle());
            if (information.getPicture() != null) {
                String savePath = PathTools.getRunPath()+"image/";
                String imagePath = savePath + information.getPicture();
                System.out.println(imagePath);
                File imageFile = new File(imagePath);
                String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1,imagePath.length());

                if (imageFile.exists()){
                    if ("jpg".equals(suffix) || "jpeg".equals(suffix)){
                        //jpg
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        String imgBase64 = ImageTools.imgToBase64(bufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }else {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        BufferedImage newBufferedImage = new BufferedImage(
                                bufferedImage.getWidth(), bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                                Color.WHITE, null);
                        String imgBase64 = ImageTools.imgToBase64(newBufferedImage);
                        jsonObject.put("picture", imgBase64);
                    }
                }else {
                    jsonObject.put("picture", "");
                }
            } else {
                jsonObject.put("picture", null);
            }
            jsonObject.put("ischoose", information.getIschoose());
            res.add(jsonObject);
        }
       return new PageInfo<>(res);
    }
}
