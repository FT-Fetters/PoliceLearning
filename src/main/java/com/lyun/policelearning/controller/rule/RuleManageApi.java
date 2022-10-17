package com.lyun.policelearning.controller.rule;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.entity.LawType;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.service.RuleService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.page.PageRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Permission
@RestController()
@RequestMapping("/rule/manage")
public class RuleManageApi {
    @Autowired
    RuleService ruleService;
    /**
     * 根据传入的PageRequest对象，返回分页之后的信息
     * @param pageQuery
     * @return
     */
    @RequestMapping(value="/getPage",method = RequestMethod.POST)
    public Object findPage(@RequestBody PageRequest pageQuery) {
        if(pageQuery.getPageSize() <= 0||pageQuery.getPageNum()<=0){
            return new ResultBody<>(false,500,"error pageSize or error pageNum");
        }
        return new ResultBody<>(true,200,ruleService.findPage(pageQuery));
    }

    @RequestMapping(value="/search",method = RequestMethod.GET)
    public Object search(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String word) {
        if(pageNum <= 0||pageSize<=0){
            return new ResultBody<>(false,500,"error pageSize or error pageNum");
        }
        PageRequest pageRequest = new PageRequest(pageNum,pageSize);
        return new ResultBody<>(true,200,ruleService.findPageSearch(pageRequest,word));
    }

    /**
     * 根据id返回新规的具体内容
     * @param id
     * @return
     */
    @Permission(admin = false)
    @RequestMapping(value = "/getPage/content",method = RequestMethod.GET)
    public Object getRuleById(@RequestParam int id){
        JSONObject res = ruleService.getRuleById(id);
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        if(id > 0){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"no found");
        }
    }

    /**
     * 根据传入rule对象，对数据库进行插入操作
     * @param rule
     * @return
     */
    @RequestMapping(value = "getPage/insert",method = RequestMethod.POST)
    public Object insertRule(@RequestBody Rule rule){
        if(ruleService.insertRule(rule)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"can't insert");
        }
    }

    /**
     * 2022.10.4
     * deng
     * 获取新规导入的模板
     */
    @SneakyThrows
    @RequestMapping(value = "/download/rule/template",method = RequestMethod.GET)
    public void getTemplate(HttpServletResponse response){
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setTitle("农村人居环境整治提升五年行动方案");
        rule.setContent("一、总体要求\n" +
                "\n" +
                "（一）指导思想。以习近平新时代中国特色社会主义思想为指导，深入贯彻党的十九大和十九届二中、三中、四中、五中、六中全会精神，坚持以人民为中心的发展思想，践行绿水青山就是金山银山的理念，深入学习推广浙江“千村示范、万村整治”工程经验，以农村厕所革命、生活污水垃圾治理、村容村貌提升为重点，巩固拓展农村人居环境整治三年行动成果，全面提升农村人居环境质量，为全面推进乡村振兴、加快农业农村现代化、建设美丽中国提供有力支撑。\n" +
                "\n" +
                "（二）工作原则\n" +
                "\n" +
                "——坚持因地制宜，突出分类施策...");
        rule.setDate("2021-12-06");
        rules.add(rule);
        //设置头属性  设置文件名称
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(Rule.class)
                .sheet("模板")
                .doWrite(rules);
    }
    /**
     * 2022 10.4
     * deng
     * 新规批量导入
     */
    @SneakyThrows
    @RequestMapping(value = "/rule/import/excel",method = RequestMethod.POST)
    public Object importType(@RequestParam MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        List<Rule> rules = EasyExcel.read(file.getInputStream()).head(Rule.class).sheet().doReadSync();
        for (Rule rule : rules){
            ruleService.insertRule(rule);
        }
        return new ResultBody<>(true,200,null);
    }
    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    @RequestMapping(value = "getPage/delete",method = RequestMethod.GET)
    public Object deleteById(@RequestParam int id){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }if(ruleService.deleteById(id)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"can't delete");
        }
    }

    /**
     * 根据id和传入的rule更新rule
     * @param rule
     * @return
     */
    @RequestMapping(value = "getPage/content/update",method = RequestMethod.POST)
    public Object updateById(@RequestBody Rule rule){
        System.out.println(rule.getTitle());
        if(rule == null){
            return new ResultBody<>(false,500,"error id or error rule");
        }if(ruleService.updateById(rule)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"can't update");
        }
    }

    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,ruleService.count());
    }
}
