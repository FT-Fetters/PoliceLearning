package com.lyun.policelearning.controller.question;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.question.MultipleChoiceService;
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
@RequestMapping("/multipleChoice/manage")
@RestController
public class MultipleChoiceManageApi {

    @Autowired
    MultipleChoiceService multipleChoiceService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        if (answer.length() > 4){
            return new ResultBody<>(false,503,"error answer len");
        }
        MultipleChoice multipleChoice = new MultipleChoice();
        multipleChoice.setProblem(problem);
        multipleChoice.setOptionA(option_a);
        multipleChoice.setOptionB(option_b);
        multipleChoice.setOptionC(option_c);
        multipleChoice.setOptionD(option_d);
        multipleChoice.setAnswer(answer);
        multipleChoiceService.newQuestion(multipleChoice);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request){
        Integer id = data.getInteger("id");
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (multipleChoiceService.getById(id) == null){
            return new ResultBody<>(false,500,"id not found");
        }
        if (problem == null){
            return new ResultBody<>(false,501,"problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null){
            return new ResultBody<>(false,502,"at least one option is required");
        }
        MultipleChoice multipleChoice = multipleChoiceService.getById(id);
        multipleChoice.setId(id);
        multipleChoice.setProblem(problem);
        multipleChoice.setOptionA(option_a);
        multipleChoice.setOptionB(option_b);
        multipleChoice.setOptionC(option_c);
        multipleChoice.setOptionD(option_d);
        multipleChoice.setAnswer(answer);
        multipleChoiceService.updateQuestion(multipleChoice);
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
        multipleChoiceService.deleteQuestion(id);
        return new ResultBody<>(true,200,null);
    }


    @RequestMapping("/delete/batch")
    public Object batchDelete(@RequestBody JSONObject data){
        if (data == null || data.getJSONArray("list") == null){
            return new ResultBody<>(false,200,"list is null");
        }
        multipleChoiceService.batchDelete(data.getJSONArray("list"));
        return new ResultBody<>(true,200,null);


    }

    @PostMapping("/select")
    public Object selectByPage(@RequestBody PageRequest pageRequest){
        if (pageRequest.getPageNum() < 0 || pageRequest.getPageSize() < 0)
            return new ResultBody<>(false, -1, "error parameter");
        return new ResultBody<>(true,200,multipleChoiceService.selectByPage(pageRequest));
    }

    @SneakyThrows
    @PostMapping("/import")
    public Object importQuestion(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        List<MultipleChoice> multipleChoices = EasyExcel.read(file.getInputStream()).head(MultipleChoice.class).sheet().doReadSync();
        return new ResultBody<>(true,200,new int[]{multipleChoices.size(),multipleChoiceService.importQuestion(multipleChoices)});
    }

    @SneakyThrows
    @GetMapping("/download/template")
    public void getTemplate(HttpServletResponse response){
        List<MultipleChoice> multipleChoices = new ArrayList<>();
        MultipleChoice multipleChoice = new MultipleChoice();
        multipleChoice.setProblem("多选题问题()");
        multipleChoice.setOptionA("选项A");
        multipleChoice.setOptionB("选项B");
        multipleChoice.setOptionC("选项C");
        multipleChoice.setOptionD("选项D");
        multipleChoice.setAnswer("AB");
        multipleChoices.add(multipleChoice);
        //设置头属性  设置文件名称
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(MultipleChoice.class)
                .sheet("模板")
                .doWrite(multipleChoices);

    }


}
