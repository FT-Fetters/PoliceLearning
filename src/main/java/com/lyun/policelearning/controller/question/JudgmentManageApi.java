package com.lyun.policelearning.controller.question;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import com.lyun.policelearning.utils.page.PageRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Permission
@RequestMapping("/judgment/manage")
@RestController
public class JudgmentManageApi {


    @Autowired
    JudgmentService judgmentService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String problem = data.getString("problem");
        String option_true = data.getString("option_true");
        String option_false = data.getString("option_false");
        String answer = data.getString("answer");
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_false == null && option_true == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        if (answer.length() != 1){
            return new ResultBody<>(false,503,"answer is too long");
        }
        Judgment judgment = new Judgment();
        judgment.setProblem(problem);
        judgment.setOption_false(option_false);
        judgment.setOption_true(option_true);
        judgment.setAnswer(answer);
        judgmentService.newQuestion(judgment);
        return new ResultBody<>(true,200,null);
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        Integer id = data.getInteger("id");
        String problem = data.getString("problem");
        String option_true = data.getString("option_true");
        String option_false = data.getString("option_false");
        String answer = data.getString("answer");
        if (judgmentService.getById(id) == null){
            return new ResultBody<>(false,500,"id not found");
        }
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_false == null && option_true == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        Judgment judgment = new Judgment();
        judgment.setId(id);
        judgment.setProblem(problem);
        judgment.setOption_false(option_false);
        judgment.setOption_true(option_true);
        judgment.setAnswer(answer);
        judgmentService.updateQuestion(judgment);
        return new ResultBody<>(true,200,null);
    }
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteQuestion(@RequestBody JSONObject data,HttpServletRequest request){
        Integer id = data.getInteger("id");
        if (id == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        judgmentService.deleteQuestion(id);
        return new ResultBody<>(true,200,null);
    }

    @PostMapping("/select")
    public Object selectByPage(@RequestBody PageRequest pageRequest){
        if (pageRequest.getPageNum() < 0 || pageRequest.getPageSize() < 0)
            return new ResultBody<>(false, -1, "error parameter");
        return new ResultBody<>(true,200,judgmentService.selectByPage(pageRequest));
    }

    @SneakyThrows
    @PostMapping("/import")
    public Object importQuestion(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        List<Judgment> judgments = EasyExcel.read(file.getInputStream()).head(Judgment.class).sheet().doReadSync();
        return new ResultBody<>(true,200,new int[]{judgments.size(),judgmentService.importQuestion(judgments)});
    }

    @SneakyThrows
    @GetMapping("/download/template")
    public void getTemplate(HttpServletResponse response){
        List<Judgment> judgments = new ArrayList<>();
        Judgment judgment = new Judgment();
        judgment.setProblem("判断题问题()");
        judgment.setAnswer("1");
        judgment.setOption_true("正确");
        judgment.setOption_false("错误");
        judgments.add(judgment);
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream()).head(Judgment.class).sheet("模板").doWrite(judgments);
    }



}
