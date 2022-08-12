package com.lyun.policelearning.controller.paper;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.controller.paper.model.PaperSubmitBody;
import com.lyun.policelearning.entity.Paper;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.paper.ExamService;
import com.lyun.policelearning.service.paper.PaperService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import com.lyun.policelearning.utils.page.PageRequest;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Permission()
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
    public Object selectAll(@RequestBody PageRequest pageQuery) {
        return new ResultBody<>(true, 200,paperService.selectAll(pageQuery));
    }

    /**
     * 用户获取所有试卷列表
     */
    @Permission(admin = false)
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
                           @RequestParam String title,HttpServletRequest request){
        int user_id = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,paperService.generate(j,m,s,title,user_id));
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
    @Permission(admin = false)
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

    @Permission(admin = false)
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

    @Permission(admin = false)
    @GetMapping("/exam/score/{paper_id}")
    public Object getExamScore(@PathVariable int paper_id,HttpServletRequest request){
        int user_id = UserUtils.getUserId(request,jwtConfig);
        return new ResultBody<>(true,200,examService.getExamScore(paper_id,user_id));
    }

    @Permission(admin = false)
    @GetMapping("/exam/grades/all/{paper_id}")
    public Object exportGrades(@PathVariable int paper_id){
        return new ResultBody<>(true,200,examService.selectPaperGrades(paper_id));
    }


    @GetMapping("/export/{id}")
    public void exportPaper(HttpServletResponse response, @PathVariable int id){
        JSONObject paper = paperService.getById(id);
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph firstParagraph = document.createParagraph();
        firstParagraph.getStyleID();
        XWPFRun run = firstParagraph.createRun();

        run.setFontSize(28);
        run.setBold(true);
        run.setText(paper.getString("title") + "\r\n");
        run = firstParagraph.createRun();
        run.addCarriageReturn();

        run.setFontSize(14);
        run.setBold(false);

        run.setText("一、单选题\n");
        int i = 1;
        for (Object o : paper.getJSONObject("question").getJSONArray("s")) {
            SingleChoice single = ((SingleChoice) o);
            if (single == null)continue;
            run.setText(i + "." + single.getProblem());
            run.addCarriageReturn();
            run.setText("A." + single.getOption_a());
            run.addCarriageReturn();
            run.setText("B." + single.getOption_b());
            run.addCarriageReturn();
            run.setText("C." + single.getOption_c());
            run.addCarriageReturn();
            run.setText("D." + single.getOption_d());
            run.addCarriageReturn();
            i++;
        }
        run.setText("二、多选题\n");
        i = 1;
        for (Object o : paper.getJSONObject("question").getJSONArray("m")) {
            MultipleChoice multiple = ((MultipleChoice) o);
            if (multiple == null)continue;
            run.setText(i + "." + multiple.getProblem());
            run.addCarriageReturn();
            run.setText("A." + multiple.getOption_a());
            run.addCarriageReturn();
            run.setText("B." + multiple.getOption_b());
            run.addCarriageReturn();
            run.setText("C." + multiple.getOption_c());
            run.addCarriageReturn();
            run.setText("D." + multiple.getOption_d());
            run.addCarriageReturn();
            i++;
        }
        run.setText("三、判断题\n");
        i = 1;
        for (Object o : paper.getJSONObject("question").getJSONArray("j")) {
            Judgment judgment = ((Judgment) o);
            if (judgment == null)continue;
            run.setText(i + "." + judgment.getProblem());
            run.addCarriageReturn();
            run.setText("A." + judgment.getOption_true());
            run.addCarriageReturn();
            run.setText("B." + judgment.getOption_false());
            run.addCarriageReturn();
            i++;
        }
        try {
            String fileName = paper.getString("title")+".docx";
            fileName = URLEncoder.encode(fileName,"UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Cache-Control", "no-cache");
            //输出流
            OutputStream os = response.getOutputStream();
            document.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
