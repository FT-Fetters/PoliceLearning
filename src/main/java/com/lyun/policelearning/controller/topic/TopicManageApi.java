package com.lyun.policelearning.controller.topic;

import com.lyun.policelearning.dao.UserDao;
import com.lyun.policelearning.entity.Topic;
import com.lyun.policelearning.service.TopicService;
import com.lyun.policelearning.utils.Constant;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic/manage")
public class TopicManageApi {
    @Autowired
    TopicService topicService;
    @Autowired
    UserDao userDao;

    /**
     * 分页返回话题列表（id、realName、title、date、picture）
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object all(@RequestParam Integer pageNum,@RequestParam Integer pageSize,
                      @RequestParam(required = false) String title){
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        return new ResultBody<>(true,200,topicService.findPage(pageRequest, title));
    }

    /**
     * 返回具体信息
     */
    @RequestMapping(value = "getById",method = RequestMethod.GET)
    public Object getById(@RequestParam int id){
        Topic topic = topicService.getById(id);
        topic.setRealName(userDao.getById(topic.getUid()).getRealname());
        if (topic.getPicture() != null && !"".equals(topic.getPicture())){
            topic.setPicture(Constant.BASE_URL + "api/upload/topicPicture/" + topic.getPicture());
        }
        return new ResultBody<>(true,200,topic);
    }

    /**
     * 根据id返回评论
     */
    @RequestMapping(value = "/getComment",method = RequestMethod.GET)
    public Object getComment(@RequestParam int id){
        return new ResultBody<>(true,200,topicService.getComment(id));
    }

    /**
     * 删除评论
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int id){
        topicService.delete(id);
        return new ResultBody<>(true,200,null);
    }
}
