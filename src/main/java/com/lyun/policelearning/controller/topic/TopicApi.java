package com.lyun.policelearning.controller.topic;

import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.Topic;
import com.lyun.policelearning.service.TopicService;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

@RestController
@RequestMapping("/topic")
public class TopicApi {
    @Autowired
    TopicService topicService;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 发布话题
     * title、content、picture
     */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public Object publish(@RequestBody Topic topic, HttpServletRequest request){
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
        int userId = UserUtils.getUserId(request,jwtConfig);
        if (Integer.valueOf(userId) == null){
            return new ResultBody<>(false,500,"请先登录");
        }
        topic.setDate(date);
        topic.setUid(userId);
        topicService.publish(topic);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 上传图片
     */
    @SneakyThrows
    @RequestMapping(value = "/upload/picture",method = RequestMethod.POST)
    public Object upload(@RequestParam MultipartFile file){
        //删除
        String filename;
        //更新
        filename = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = filename.substring(filename.lastIndexOf("."));
        //重新命名文件
        filename= UUID.randomUUID()+suffixName;
        String savePath = PathTools.getRunPath()+"/upload/topicPicture/";
        if(!new File(savePath).exists()){
            new File(savePath).mkdirs();
        }
        File targetFile = new File(PathTools.getRunPath()+"/upload/topicPicture");
        File saveFile = new File(targetFile, filename);
        file.transferTo(saveFile);
        return new ResultBody<>(true,200,filename);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int id){
        //先删除图片
        String filename = topicService.getById(id).getPicture();
        String path = PathTools.getRunPath() +"/upload/topicPicture/"+ filename;
        File file1 = new File(path);
        if(file1.exists()){
            file1.delete();
        }
        //然后再删除话题
        topicService.delete(id);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 返回话题列表(id、title、realName、picture(url)、date)
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String title){
        return new ResultBody<>(true,200,topicService.list(title));
    }

    /**
     * 根据id返回具体的话题信息(id、title、content、picture、date、topicComment)
     */
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public Object getById(@RequestParam int id){
        return topicService.getDetail(id);
    }
}
