package com.lyun.policelearning.controller.tool;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.entity.Tool;
import com.lyun.policelearning.service.ToolService;
import com.lyun.policelearning.utils.Constant;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RequestMapping("/tool/manage")
public class ToolManageApi {
    @Autowired
    ToolService toolService;

    /**
     * 根据类型返回分页列表（根据标题进行搜索）
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object all(@RequestParam Integer pageNum,@RequestParam Integer pageSize,
                      @RequestParam Integer type,@RequestParam(required = false) String title){
        PageRequest pageRequest = new PageRequest(pageNum,pageSize);
        return new ResultBody<>(true,200,toolService.getAll(pageRequest,type,title));
    }


    /**
     * 根据id返回具体信息
     */
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public Object getById(@RequestParam Integer id){
        return new ResultBody<>(true,200,toolService.getById(id));
    }


    /**
     * 新增司法 title、content、type
     */
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public Object insert(@RequestBody Tool tool){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        tool.setDate(date);
        toolService.insert(tool);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 根据id更新司法 title、content、id
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object update(@RequestBody Tool tool){
        if (tool.getId() == null){
            return new ResultBody<>(false,400,"缺少参数id");
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        tool.setDate(date);
        toolService.update(tool);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 删除司法
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int id){
        toolService.delete(id);
        return new ResultBody<>(true,200,null);
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
                return new ToolManageApi.ErrorResponse(1,"图片过大");
            }

            fileName = System.currentTimeMillis()+suffix;
            if(!new File(PathTools.getRunPath()+"/upload").exists()){
                new File(PathTools.getRunPath()+"/upload").mkdir();
            }
            String saveFileName = PathTools.getRunPath() +"/upload/toolPicture/"+fileName;
            File dest = new File(saveFileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new ToolManageApi.ErrorResponse(1,e.getMessage());
            }
        }else {
            return new ToolManageApi.ErrorResponse(1,"其它问题，请联系管理员");
        }
        InetAddress address = InetAddress.getLocalHost();
        String imgUrl = Constant.BASE_URL + "api/upload/toolPicture/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",imgUrl);
        return new ToolManageApi.PictureResponse(0,jsonObject);
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
                return new ToolManageApi.ErrorResponse(1,"文件没有后缀名，请重新上传");
            }

            fileName = System.currentTimeMillis()+suffix;
            if(!new File(PathTools.getRunPath()+"/upload").exists()){
                new File(PathTools.getRunPath()+"/upload").mkdir();
            }
            String saveFileName = PathTools.getRunPath() +"/upload/toolVideo/"+fileName;
            File dest = new File(saveFileName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return new ToolManageApi.ErrorResponse(1,e.getMessage());
            }
        }else {
            return new ToolManageApi.ErrorResponse(1,"上传出错");
        }
        String videoUrl = Constant.BASE_URL + "api/upload/toolVideo/" + fileName;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",videoUrl);
        return new ToolManageApi.WangEditorResponse(0,jsonObject);
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
