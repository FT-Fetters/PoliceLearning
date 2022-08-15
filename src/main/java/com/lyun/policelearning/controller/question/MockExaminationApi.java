package com.lyun.policelearning.controller.question;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.dao.question.SingleChoiceDao;
import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import com.lyun.policelearning.service.paper.PaperService;
import com.lyun.policelearning.service.question.JudgmentService;
import com.lyun.policelearning.service.question.MultipleChoiceService;
import com.lyun.policelearning.service.question.SingleChoiceService;
import com.lyun.policelearning.utils.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/mock")
@RestController
public class MockExaminationApi {

    private static final int QUESTION_NUM = 10;
    @Autowired
    SingleChoiceService singleChoiceService;

    @Autowired
    MultipleChoiceService multipleChoiceService;

    @Autowired
    JudgmentService judgmentService;

    @Autowired
    PaperService paperService;

    @SneakyThrows
    @RequestMapping("/sample")
    public Object sample(HttpServletRequest request){
        List<JSONObject> singleChoiceList = QuestionUtils.sampleSingle(singleChoiceService,QUESTION_NUM);
        List<JSONObject> multipleChoiceList = QuestionUtils.sampleMultiple(multipleChoiceService,QUESTION_NUM);
        List<JSONObject> judgmentList = QuestionUtils.sampleJudgment(judgmentService,QUESTION_NUM);
        JSONObject res = new JSONObject();
        res.put("single",singleChoiceList);
        res.put("multiple",multipleChoiceList);
        res.put("judgment",judgmentList);
        LogUtils.log("get mock examination","get",true,request);
        File dir =  new File(PathTools.getRunPath() + "/mock");
        File file = new File(PathTools.getRunPath() + "/mock/count.txt");
        boolean writeable = file.setWritable(true, false);
        if (file.exists()){
            String str = FileUtils.txt2String(file).replace("\r\n","").replace("\n","").replace("\r","");
            int count = Integer.parseInt(str);
            FileWriter fileWriter = new FileWriter(file,false);
            fileWriter.write(String.valueOf(count+1));
            fileWriter.close();;
        }else {
            boolean mkdirs = dir.mkdirs();
            boolean newFile = file.createNewFile();
            if (newFile){
                FileWriter fileWriter = new FileWriter(file,false);
                fileWriter.write("1");
                fileWriter.close();
            }
        }
        return new ResultBody<>(true,200,res);
    }


    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        /*try {
            File file = new File(PathTools.getRunPath() + "/mock/count.txt");
            boolean b = file.setWritable(true, false);
            if (file.exists()){
                String str = FileUtils.txt2String(file).replace("\r\n","").replace("\n","").replace("\r","");
                int count = Integer.parseInt(str);
//                int count = Integer.parseInt(FileUtils.txt2String(file));
                return new ResultBody<>(true,200,count);
            }else {
                return new ResultBody<>(true,200,0);
            }
        }catch (IOException e) {
            e.printStackTrace();
            return new ResultBody<>(false,-1,"unknown error");
        }*/
        //计算出试卷的数量
        return new ResultBody<>(true,200,paperService.count());
    }

}
