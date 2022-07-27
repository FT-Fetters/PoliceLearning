package com.lyun.policelearning.controller.paper;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.controller.paper.model.PaperSubmitBody;
import com.lyun.policelearning.entity.Paper;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.paper.ExamService;
import com.lyun.policelearning.service.paper.PaperService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/paper")
public class PaperApi {

    @Autowired
    PaperService paperService;

    @Autowired
    UserService userService;

    @Autowired
    ExamService examService;

    @Autowired
    JwtConfig jwtConfig;

    /**
     * 管理员获取所有试卷列表
     */
    @GetMapping ("/select/all")
    public Object selectAll(){
        List<Paper> papers = paperService.selectAll();
        return new ResultBody<>(true,200,papers);
    }

    /**
     * 用户获取所有试卷列表
     */
    @GetMapping("/select/user/all")
    public Object userSelectAll(HttpServletRequest request){
        return new ResultBody<>(true,200,paperService.userSelectAll(UserUtils.getUserId(request,jwtConfig)));
    }

    /**
     * 生成试卷
     * @param j 判断题数量
     * @param m 多选题数量
     * @param s 单选题数量
     * @param title 试卷标题
     * @return 试卷id
     */
    @PostMapping("/generate")
    public Object generate(@RequestParam Integer j,@RequestParam Integer m,@RequestParam Integer s,
                           @RequestParam String title){
        return new ResultBody<>(true,200,paperService.generate(j,m,s,title));
    }

    /**
     * 管理员获取指定的试卷
     * @param id 试卷id
     */
    @GetMapping("/get/{id}")
    public Object getById(@PathVariable int id){
        return new ResultBody<>(true,200,paperService.getById(id));
    }

    /**
     * 用户获取指定的试卷
     * @param id 试卷的id
     */
    @GetMapping("/get/user/{id}")
    public Object userGetById(@PathVariable int id){
        JSONObject paper = paperService.userGetById(id);
        if (paper == null){
            return new ResultBody<>(true,-1,"unknown id or paper is disable");
        }else {
            return new ResultBody<>(true,200,paper);
        }
    }

    /**
     * 删除试卷
     * @param id 试卷的id
     */
    @RequestMapping("/delete/{id}")
    public Object delete(@PathVariable int id){
        if (paperService.delete(id)){
            return new ResultBody<>(true,200,"delete success");
        }else {
            return new ResultBody<>(true,200,"unknown id");
        }
    }

    /**
     * 启用试卷
     * @param id 试卷的id
     */
    @RequestMapping("/enable/{id}")
    public Object enable(@PathVariable int id){
        if (paperService.enable(id)){
            return new ResultBody<>(true,200,"update success");
        }else {
            return new ResultBody<>(true,200,"unknown id");
        }
    }

    /**
     * 禁用试卷
     * @param id 试卷的id
     */
    @RequestMapping("/disable/{id}")
    public Object disable(@PathVariable int id){
        if (paperService.disable(id)){
            return new ResultBody<>(true,200,"update success");
        }else {
            return new ResultBody<>(true,200,"unknown id");
        }
    }

    @PostMapping("/exam/submit")
    public Object submit(@RequestBody PaperSubmitBody paperSubmitBody,HttpServletRequest request){
        int user_id = UserUtils.getUserId(request,jwtConfig);
        paperSubmitBody.setUser_id(user_id);
        if (userService.getById(paperSubmitBody.getUser_id()) == null)
            return new ResultBody<>(true,-1,"unknown user id");
        if (paperService.getById(paperSubmitBody.getPaper_id()) == null)
            return new ResultBody<>(true,-1,"unknown paper id");
        float score = examService.submit(paperSubmitBody.getUser_id(),paperSubmitBody.getPaper_id(),paperSubmitBody.getInputs());
        if (score < 0){
            return new ResultBody<>(true,200,"user already finished exam");
        }else {
            return new ResultBody<>(true,200, score);
        }
    }

    @GetMapping("/exam/list")
    public Object finishExamList(HttpServletRequest request){
        int user_id = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,examService.selectByUserId(user_id));
    }

    @GetMapping("/exam/score/{paper_id}")
    public Object getExamScore(@PathVariable int paper_id,HttpServletRequest request){
        int user_id = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,examService.getExamScore(paper_id,user_id));
    }

}
