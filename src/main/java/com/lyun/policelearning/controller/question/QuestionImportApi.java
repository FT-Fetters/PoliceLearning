package com.lyun.policelearning.controller.question;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.lyun.policelearning.service.question.QuestionImportService;
import com.lyun.policelearning.utils.FileUtils;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@RequestMapping("/question")
public class QuestionImportApi {

    @Autowired
    private WFGIdGenerator wFGIdGenerator;

    @Autowired
    private QuestionImportService questionImportService;

    @SneakyThrows
    @PostMapping("/word/import")
    public Object importWord(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        String fileName = wFGIdGenerator.next() + ".docx";
        String filePath = PathTools.getRunPath()+ "/upload/" + fileName;
        file.transferTo(new File(filePath));
        questionImportService.importQuestion(filePath);
        return new ResultBody<>(true,200,"success");
    }


    @SneakyThrows
    @GetMapping("/download/template")
    public void getTemplate(HttpServletResponse response){
        ServletOutputStream ops = response.getOutputStream();
        response.reset();
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader(
                "Content-disposition",
                "attachment; filename=template.docx");
        byte[] b = FileUtils.getResourceFile("/templates/questionsImport.docx", this.getClass().getClassLoader());
        ops.write(b);
    }


}
