package com.lyun.policelearning.controller.topic;

import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.TopicComment;
import com.lyun.policelearning.service.TopicCommentService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RequestMapping("/topic/comment")
public class TopicCommentApi {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private TopicCommentService topicCommentService;

    /**
     * 发表评论tid、replay
     */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public Object publish(@RequestBody TopicComment topicComment, HttpServletRequest request){
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
        int userId = UserUtils.getUserId(request,jwtConfig);
        topicComment.setDate(date);
        topicComment.setUid(userId);
        topicCommentService.publish(topicComment);
        return new ResultBody<>(true,200,null);
    }

    /**
     * 删除评论
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int id){
        topicCommentService.delete(id);
        return new ResultBody<>(true,200,null);
    }
}
