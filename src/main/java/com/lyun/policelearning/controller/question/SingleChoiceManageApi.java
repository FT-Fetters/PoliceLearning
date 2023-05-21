package com.lyun.policelearning.controller.question;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.service.question.SingleChoiceService;
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
@RequestMapping("/singleChoice/manage")
@RestController
public class SingleChoiceManageApi {

    @Autowired
    SingleChoiceService singleChoiceService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Object newQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request) {
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (problem == null) {
            return new ResultBody<>(false, 501, "problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null) {
            return new ResultBody<>(false, 502, "at least one option is required");
        }
        if (answer.length() != 1) {
            return new ResultBody<>(false, 503, "answer is too long");
        }
        SingleChoice singleChoice = new SingleChoice();
        singleChoice.setProblem(problem);
        singleChoice.setOptionA(option_a);
        singleChoice.setOptionB(option_b);
        singleChoice.setOptionC(option_c);
        singleChoice.setOptionD(option_d);
        singleChoice.setAnswer(answer);
        singleChoiceService.newQuestion(singleChoice);
        return new ResultBody<>(true, 200, null);
    }

    @RequestMapping("/update")
    public Object updateQuestion(@RequestBody JSONObject data, HttpServletResponse response, HttpServletRequest request) {
        Integer id = data.getInteger("id");
        String problem = data.getString("problem");
        String option_a = data.getString("option_a");
        String option_b = data.getString("option_b");
        String option_c = data.getString("option_c");
        String option_d = data.getString("option_d");
        String answer = data.getString("answer");
        if (singleChoiceService.getById(id) == null) {
            return new ResultBody<>(false, 500, "id not found");
        }
        if (problem == null) {
            return new ResultBody<>(false, 501, "problem can not null");
        }
        if (option_a == null && option_b == null && option_c == null && option_d == null) {
            return new ResultBody<>(false, 502, "at least one option is required");
        }
        SingleChoice singleChoice = singleChoiceService.getById(id);
        singleChoice.setId(id);
        singleChoice.setProblem(problem);
        singleChoice.setOptionA(option_a);
        singleChoice.setOptionB(option_b);
        singleChoice.setOptionC(option_c);
        singleChoice.setOptionD(option_d);
        singleChoice.setAnswer(answer);
        singleChoiceService.updateQuestion(singleChoice);
        return new ResultBody<>(true, 200, null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object deleteQuestion(@RequestBody JSONObject data, HttpServletRequest request) {
        Integer id = data.getInteger("id");
        if (id == null) {
            return new ResultBody<>(false, 500, "missing parameter");
        }
        if (id <= 0) {
            return new ResultBody<>(false, 500, "error id");
        }
        singleChoiceService.deleteQuestion(id);
        return new ResultBody<>(true, 200, null);
    }

    @RequestMapping("/delete/batch")
    public Object batchDelete(@RequestBody JSONObject data) {
        if (data == null || data.getJSONArray("list") == null) {
            return new ResultBody<>(false, 200, "list is null");
        }
        singleChoiceService.batchDelete(data.getJSONArray("list"));
        return new ResultBody<>(true, 200, null);
    }

    @PostMapping("/select")
    public Object selectByPage(@RequestBody PageRequest pageRequest) {
        if (pageRequest.getPageNum() < 0 || pageRequest.getPageSize() < 0)
            return new ResultBody<>(false, -1, "error parameter");
        return new ResultBody<>(true, 200, singleChoiceService.selectByPage(pageRequest));
    }


    @SneakyThrows
    @PostMapping("/import")
    public Object importQuestion(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResultBody<>(true, -1, "empty file");
        }
        List<SingleChoice> singleChoices = EasyExcel.read(file.getInputStream()).head(SingleChoice.class).sheet().doReadSync();
        return new ResultBody<>(true, 200, new int[]{singleChoices.size(), singleChoiceService.importQuestion(singleChoices)});
    }

    @SneakyThrows
    @GetMapping("/download/template")
    public void getTemplate(HttpServletResponse response) {
        List<SingleChoice> singleChoices = new ArrayList<>();
        SingleChoice singleChoice = new SingleChoice();
        singleChoice.setProblem("单选题问题()");
        singleChoice.setOptionA("选项A");
        singleChoice.setOptionB("选项B");
        singleChoice.setOptionC("选项C");
        singleChoice.setOptionD("选项D");
        singleChoice.setAnswer("A");
        singleChoices.add(singleChoice);
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream()).head(SingleChoice.class).sheet("模板").doWrite(singleChoices);

    }

}
