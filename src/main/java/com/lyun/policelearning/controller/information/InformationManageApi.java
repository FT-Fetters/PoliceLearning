package com.lyun.policelearning.controller.information;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.controller.course.TeachApi;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.service.InformationService;
import com.lyun.policelearning.utils.ImageTools;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Permission(admin = true)
@RestController
@RequestMapping("/information/manage")
public class InformationManageApi {
    @Autowired
    InformationService informationService;

    /**
     * 根据传入的PageRequest对象，返回分页之后的信息
     * @param pageQuery
     * @return
     */
    @Permission(admin = true)
    @RequestMapping(value="/getPage",method = RequestMethod.POST)
    public Object findPage(@RequestBody PageRequest pageQuery, HttpServletResponse response) {
        if(pageQuery.getPageSize() <= 0||pageQuery.getPageNum()<=0){
            response.setHeader("Access-Control-Allow-Origin", "*");
            return new ResultBody<>(false,500,"error pageSize or error pageNum");
        }
        return new ResultBody<>(true,200,informationService.findPage(pageQuery));
    }


    /**
     * 根据id返回资讯的具体内容
     * @param id
     * @return
     */
    @Permission(admin = true)
    @RequestMapping(value = "/getPage/content",method = RequestMethod.GET)
    public Object getRuleById(@RequestParam int id) throws Exception {
        JSONObject res = informationService.getInformationById(id);
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if(id > 0){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"no found");
        }
    }

    /**
     * 根据传入information对象，对数据库进行插入操作
     * @param
     * @return
     */
    @RequestMapping(value = "/getPage/insert",method = RequestMethod.POST)
    public Object insertInformation(@RequestBody Information information) throws IOException {
        /*String img64 = information.getPicture();
        BufferedImage file1 = ImageTools.base64ToImg(img64);*/
        /*String filename = null;
        String filepath = PathTools.getRunPath()+"/image";
        String savePath = PathTools.getRunPath()+"/image/";
        if(!new File(savePath).exists()){
            new File(savePath).mkdirs();
        }
        filename = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = filename.substring(filename.lastIndexOf("."));
        //重新命名文件
        filename= UUID.randomUUID()+suffixName;
        File targetFile = new File(filepath);
        File saveFile = new File(targetFile, filename);
        file.transferTo(saveFile);*/
        if(informationService.insertInformation(information)){
            JSONObject res = new JSONObject();
            res.put("id",informationService.getInformationByTitle(information.getTitle()).getId());
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,500,"can't insert");
        }
    }

    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPage/delete",method = RequestMethod.GET)
    public Object deleteById(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        String path = informationService.getPictureById(id);
        path = PathTools.getRunPath() +"/image/"+ path;
        File file = new File(path);
        if(file.exists()){
            file.delete();
            if(informationService.deleteById(id)){
                return new ResultBody<>(true,200,null);
            }else {
                return new ResultBody<>(false,501,"can't delete");
            }
        }else {
            if(informationService.deleteById(id)){
                return new ResultBody<>(true,200,null);
            }else {
                return new ResultBody<>(false,501,"can't delete");
            }
        }

    }

    /**
     * 根据传入的information更新information
     * @param
     * @return
     */
    @RequestMapping(value = "/getPage/content/update",method = RequestMethod.POST)
    public Object updateById(@RequestBody JSONObject data){
        String title = data.getString("title");
        String content = data.getString("content");
        Date date =   Date.valueOf(data.getString("date"));
        Integer id  = data.getInteger("id");
        Integer view = data.getInteger("view");
        Information information = new Information();
        information.setId(id);
        information.setContent(content);
        information.setDate(date);
        information.setTitle(title);
        information.setView(view);
        informationService.updateById(information);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 更换图片
     * @return
     */
    @RequestMapping(value = "/getPage/content/updatePicture",method = RequestMethod.POST)
    public Object updatePicById(int id,@RequestParam("file") MultipartFile file) throws IOException {
        //删除
        String filename;
        if (id != -1){
            filename = informationService.getPictureById(id);
            String path = PathTools.getRunPath() +"/image/"+ filename;
            File file1 = new File(path);
            if(file1.exists()){
                file1.delete();
            }
        }
        //更新
        filename = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = filename.substring(filename.lastIndexOf("."));
        //重新命名文件
        filename= UUID.randomUUID()+suffixName;
        String savePath = PathTools.getRunPath()+"/image/";
        if(!new File(savePath).exists()){
            new File(savePath).mkdirs();
        }
        File targetFile = new File(PathTools.getRunPath()+"/image");
        File saveFile = new File(targetFile, filename);
        file.transferTo(saveFile);
        //修改数据库中的文件名
        if (id != -1) {
            informationService.updatePicture(id, filename);
        }
        return new ResultBody<>(true,200,filename);
    }
    /**
     * 单独删除图片
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPage/content/deletePicture",method = RequestMethod.GET)
    public Object deletePicById(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }else {
            String filename = informationService.getPictureById(id);
            String path = PathTools.getRunPath() +"/image/"+ filename;
            File file1 = new File(path);
            if(file1.exists()){
                file1.delete();
            }
            informationService.deletePicture(id);
            return new ResultBody<>(true,200,null);
        }
    }
    /**
     * 实现置顶和取消置顶的功能
     * @param id
     * @param istop 1为置顶  0为不置顶
     * @return
     */
    @RequestMapping(value = "/getPage/setTop",method = RequestMethod.GET)
    public Object setTop(@RequestParam int id,@RequestParam int istop){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if(istop != 1 && istop != 0){
            return new ResultBody<>(false,500,"error istop");
        }
        if(informationService.updateTopById(id,istop)){
            return new ResultBody<>(true,200,null);
        }else{
            return new ResultBody<>(false,501,"can't update");
        }
    }
    /**
     * 返回三个从资讯中获取的轮播图
     * @return
     */
    @RequestMapping(value = "/getPicture",method = RequestMethod.GET)
    public Object getPicture() throws Exception {
        return new ResultBody<>(true,200,informationService.getPicture());
    }
    /**
     * 返回所有的图片（不分页）
     */
    @RequestMapping(value = "/findAllPicture",method = RequestMethod.GET)
    public Object findAllPicture(){
        return new ResultBody<>(true,200,informationService.findPicture());
    }
    /**
     * 前端设计一个下拉框从所有的资讯内容中选择
     * @return
     */
    @RequestMapping(value = "/getAllPicture",method = RequestMethod.POST)
    public Object getAllPicture(@RequestBody PageRequest pageQuery,HttpServletResponse response){
        if(pageQuery.getPageSize() <= 0||pageQuery.getPageNum()<=0){
            response.setHeader("Access-Control-Allow-Origin", "*");
            return new ResultBody<>(false,500,"error pageSize or error pageNum");
        }
        return new ResultBody<>(true,200,informationService.findPicture(pageQuery));
    }

    /**
     * 提交改变后的轮换图
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getPicture/changePicture",method = RequestMethod.GET)
    public Object changePicture(@RequestParam int[] ids){
        if (ids.length > 3){
            return new ResultBody<>(false,501,"数组大小不能超过3");
        }else {
            //将原来的ischoose重置为0
            informationService.updateChoose();
            //将提交的资讯中的ischoose设置为1
            List<Integer> ids1 = Arrays.stream(ids).boxed().collect(Collectors.toList());
            informationService.setChangePicture(ids1);
            return new ResultBody<>(true, 200, null);
        }
    }

    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,informationService.count());
    }

    @SneakyThrows
    @Permission(admin = true)
    @RequestMapping(value = "/upload/picture",method = RequestMethod.POST)
    public Object uploadPicture(@RequestParam MultipartFile file){
        String fileName ="";
        if(!file.isEmpty()){
            //返回的是字节长度,1M=1024k=1048576字节 也就是if(fileSize<5*1048576)
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(StringUtils.isBlank(suffix)){
                return new InformationManageApi.ErrorResponse(1,"图片过大");
            }

            fileName = System.currentTimeMillis()+suffix;
            if(!new File(PathTools.getRunPath()+"/upload").exists()){
                new File(PathTools.getRunPath()+"/upload").mkdir();
            }
            String saveFileName = PathTools.getRunPath() +"/upload/informationPicture/"+fileName;
            File dest = new File(saveFileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new InformationManageApi.ErrorResponse(1,e.getMessage());
            }
        }else {
            return new InformationManageApi.ErrorResponse(1,"其它问题，请联系管理员");
        }
        InetAddress address = InetAddress.getLocalHost();
        String imgUrl = "http://" + "ldqc.xyz" + ":5880/api/upload/informationPicture/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",imgUrl);
        return new InformationManageApi.PictureResponse(0,jsonObject);
    }

    @SneakyThrows
    @RequestMapping(value = "/upload/video",method = RequestMethod.POST)
    @ResponseBody
    @Permission(admin = true)
    public Object editorOfVideo(@RequestParam("file") MultipartFile file) {
        String fileName ="";
        if(!file.isEmpty()){
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(StringUtils.isBlank(suffix)){
                return new InformationManageApi.ErrorResponse(1,"文件没有后缀名，请重新上传");
            }

            fileName = System.currentTimeMillis()+suffix;
            if(!new File(PathTools.getRunPath()+"/upload").exists()){
                new File(PathTools.getRunPath()+"/upload").mkdir();
            }
            String saveFileName = PathTools.getRunPath() +"/upload/informationVideo/"+fileName;
            File dest = new File(saveFileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new InformationManageApi.ErrorResponse(1,e.getMessage());
            }
        }else {
            return new InformationManageApi.ErrorResponse(1,"上传出错");
        }
        InetAddress address = InetAddress.getLocalHost();;
        String url = address.getHostAddress();
        String videoUrl = "http://" + "ldqc.xyz" + ":5880/api/upload/informationVideo/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",videoUrl);
        return new InformationManageApi.WangEditorResponse(0,jsonObject);
    }

    @Data
    private class PictureResponse{
        int errno;
        Object data;
        public PictureResponse(int errno,Object data){
            this.errno=errno;
            this.data=data;
        }
        public PictureResponse(int errno){
            this.errno=errno;
        }
    }
    @Data
    private class ErrorResponse{
        int errno;
        Object message;
        public ErrorResponse(int errno,Object message){
            this.errno = errno;
            this.message = message;
        }
    }

    @Data
    private class WangEditorResponse{
        int errno;
        JSONObject data;
        public WangEditorResponse(int errno,JSONObject data){
            this.errno=errno;
            this.data=data;
        }
    }
}
