package com.lyun.policelearning.controller.question;

import com.github.qrpcode.domain.WordGo;
import com.hy.corecode.idgen.WFGIdGenerator;
import com.lyun.policelearning.service.question.QuestionImportService;
import com.lyun.policelearning.utils.PathTools;
import com.lyun.policelearning.utils.ResultBody;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

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
        XWPFDocument doc = new XWPFDocument();
        //创建一个段落
        XWPFParagraph para = doc.createParagraph();

        //一个XWPFRun代表具有相同属性的一个区域。
        XWPFRun run = para.createRun();
        String[] tmp = ("一、单选题\n" +
                "1.习近平在《中共中央关于党的百年奋斗重大成就和历史经验的决议》起草的有关情况说明中指出，党中央认为，党的百年奋斗历程波澜壮阔，时间跨度长，涉及范围广，需要研究的问题多。总的是要按照（）的要求。\n" +
                "A.总结历史、把握规律、迎难而上、鼓足干劲\n" +
                "B.把握规律、迎难而上、鼓足干劲、走向未来\n" +
                "C.把握规律、鼓足干劲、坚定信心、砥砺前行\n" +
                "D.总结历史、把握规律、坚定信心、走向未来\n" +
                "参考答案：D\n" +
                "二、多选题\n" +
                "1.十九届六中全会主要议程是（）\n" +
                "A.中共中央政治局向中央委员会报告工作，重点研究全面总结党的百年奋斗的重大成就和历史经验问题\n" +
                "B.审议《中共中央关于党的百年奋斗重大成就和历史经验的决议稿》\n" +
                "C《中共中央关于制定国民经济和社会发展第十四个五年规划和二〇三五年远景目标的建议》\n" +
                "D.《坚持和完善中国特色社会主义制度、推进国家治理体系和治理能力现代化》\n" +
                "参考答案：AB;\n" +
                "三、判断题\n" +
                "1.十月革命一声炮响，给中国送来了马克思列宁主义，《新青年》杂志的创办、发行，促进了马克思主义在中国的传播。\n" +
                "参考答案：错误").split("\n");
        run.setFontSize(18);
        for (String s : tmp) {
            run.setText(s);
            run.addCarriageReturn();
        }
        ServletOutputStream ops = response.getOutputStream();
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader(
                "Content-disposition",
                "attachment; filename=template.docx");
        doc.write(ops);
        ops.close();
    }


}
