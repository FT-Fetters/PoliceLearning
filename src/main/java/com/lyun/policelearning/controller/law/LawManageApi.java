package com.lyun.policelearning.controller.law;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.entity.LawType;
import com.lyun.policelearning.entity.UserTemplate;
import com.lyun.policelearning.service.LawService;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.beans.binding.ObjectExpression;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Permission(admin = true)
@RestController
@RequestMapping("/law/manage")
public class LawManageApi {
    @Autowired
    LawService lawService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;
    /**
     * 获取所有的法律类型
     * @return 返回法律类型这一列表
     */
    @RequestMapping("")
    public Object getLawType(){
        return new ResultBody<>(true,200,lawService.findAllType());
    }

    /**
     * 新增法律类型(不重复)
     */
    @RequestMapping(value = "/insertType",method = RequestMethod.POST)
    public Object insertType(@RequestParam String lawtype){
        lawService.insertType(lawtype);
        return new ResultBody<>(true,200,null);
    }
    /**
     * 2022.10.3
     * deng
     * 获取法律类型模板
     */
    @SneakyThrows
    @RequestMapping(value = "/download/lawType/template",method = RequestMethod.GET)
    public void getTemplate(HttpServletResponse response){
        List<LawType> lawTypes = new ArrayList<>();
        LawType lawType = new LawType();
        lawType.setLawtype("交通法");
        lawTypes.add(lawType);
        //设置头属性  设置文件名称
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(LawType.class)
                .sheet("模板")
                .doWrite(lawTypes);
    }
    /**
     * 2022 10.3
     * deng
     * 法律类型批量导入
     */
    @SneakyThrows
    @RequestMapping(value = "/lawType/import/excel",method = RequestMethod.POST)
    public Object importType(@RequestParam MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        List<LawType> lawTypes = EasyExcel.read(file.getInputStream()).head(LawType.class).sheet().doReadSync();
        for (LawType lawType : lawTypes){
            if (lawType.getLawtype() != null){
                lawService.insertType(lawType.getLawtype());
            }
        }
        return new ResultBody<>(true,200,null);
    }

    /**
     * 2022.10.4
     * deng
     * 获取法条模板
     */
    @SneakyThrows
    @RequestMapping(value = "/download/law/template",method = RequestMethod.GET)
    public void getTemplate1(HttpServletResponse response){
        List<Law> laws = new ArrayList<>();
        Law law = new Law();
        law.setLawtype("交通法");
        law.setTitle("《中华人民共和国道路交通安全法》");
        law.setContent("第一章　总　则\n" +
                "第二章　车辆和驾驶人\n" +
                "第一节　机动车、非机动车\n" +
                "第二节　机动车驾驶人....");
        law.setExplaination("《中华人民共和国道路交通安全法实施条例》是国务院根据《中华人民共和国道路交通安全法》 [1]  制定的，于2004年4月28日国务院第49次常务会议通过的国家法规，2004年4月30日公布，自2004年5月1日起施行。共计8章115条。");
        law.setCrime("上道路行驶的机动车未悬挂机动车号牌，未放置检验合格标志、保险标志，或者未随车携带行驶证、驾驶证的，公安机关交通管理部门应当扣留机动车，通知当事人提供相应的牌证、标志或者补办相应手续，并可以依照本法第九十条的规定予以处罚。当事人提供相应的牌证、标志或者补办相应手续的，应当及时退还机动车。");
        law.setKeyword("[{\"道路交通事故救助基金\":\"道路交通事故社会救助基金是指依法筹集用于垫付机动车道路交通事故中受害人人身伤亡的丧葬费用、部分或者全部抢救费用的社会专项基金。\"}]");
        laws.add(law);
        //设置头属性  设置文件名称
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(Law.class)
                .sheet("模板")
                .doWrite(laws);
    }

    /**
     * 2022 10.4
     * deng
     * 法律批量导入
     */
    @SneakyThrows
    @RequestMapping(value = "/law/import/excel",method = RequestMethod.POST)
    public Object importLaw(@RequestParam MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        List<Law> laws = EasyExcel.read(file.getInputStream()).head(Law.class).sheet().doReadSync();
        for (Law law : laws){
            if (law.getKeyword() != null){
                JSONArray keyWord = (JSONArray) JSONArray.parse(law.getKeyword());
                lawService.insert(law.getLawtype(),law.getTitle(),law.getContent(),law.getExplaination(),law.getCrime(),keyWord);
            }else {
                lawService.insert(law.getLawtype(),law.getTitle(),law.getContent(),law.getExplaination(),law.getCrime(),null);
            }
        }
        return new ResultBody<>(true,200,null);
    }

    /**
     * 删除法律类型
     */
    @RequestMapping(value = "/lawtype/delete",method = RequestMethod.GET)
    public Object deleteType(@RequestParam int id){
        if (id <= 0){
            return new ResultBody<>(false,501,"error id");
        }
        lawService.deleteType(id);
        return new ResultBody<>(true,200,null);
    }
    /**
     * 更新法律类型
     */
    @RequestMapping(value = "/lawtype/update",method = RequestMethod.POST)
    public Object updateType(@RequestParam int id,@RequestParam String type){
        if(id <= 0){
            return new ResultBody<>(false,500,"error id");
        }
        lawService.updateType(id, type);
        return new ResultBody<>(true,200,null);
    }
    /**
     * 根据法律的类型查询对应的title有哪些
     * @param lawtype
     * @return 返回对应的title目录
     */
    @RequestMapping(value = "/catalogue",method = RequestMethod.GET)
    public Object getCatalogueByType(@RequestParam String lawtype, HttpServletRequest request){
        if(lawtype == null){
            return new ResultBody<>(false,500,"error type");
        }
        List<JSONObject> res = lawService.findTitleByNameForManage(lawtype);
        if (res != null){
            return new ResultBody<>(true,200,res);
        }else {
            return new ResultBody<>(false,501,"unknown type");
        }
    }
    /**
     * 根据传入的title查询法律的所有内容
     * @param title
     * @return 返回该tittle所对应的法律的所有内容
     */
    @RequestMapping(value = "/content",method = RequestMethod.GET)
    public Object getContent(@RequestParam String title) {
        if (title == null) {
            return new ResultBody<>(false, 500, "error title");
        }
        JSONObject res = lawService.findContent(title);
        if (res != null) {
            return new ResultBody<>(true, 200, res);
        } else {
            return new ResultBody<>(false, 501, "unknown title");
        }
    }
    /**
     *实现上一条和下一条的功能
     * @param id 根据传入的id进行查找
     * @return  返回该id所对应的法律内容
     */
    @RequestMapping(value = "/content/id",method = RequestMethod.GET)
    public Object getContentById(@RequestParam int id){
        if (id <= 0) {
            return new ResultBody<>(false, 500, "error id");
        }
        JSONObject res = lawService.findContentById(id);
        if (res != null) {
            return new ResultBody<>(true, 200, res);
        } else {
            return new ResultBody<>(false, 501, "unknown id");
        }
    }
    /**
     * 实现新增词类功能
     * @return
     */
    @RequestMapping(value = "/content/updateKeyword",method = RequestMethod.POST)
    public Object updateKeyword(@RequestBody JSONObject data,@RequestParam int id){
        String name = data.getString("name");
        String explain = data.getString("explain");

        if(name == null || explain == null){
            return new ResultBody<>(false,500,"missing parameter");
        }
        if (lawService.updateKeyword(name,explain,id)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"unknown error");
        }
    }

    /**
     * 插入一条法律
     * @param data
     * @return
     */
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public Object insert(@RequestBody JSONObject data){
        String lawtype = data.getString("lawtype");
        String title = data.getString("title");
        String content = data.getString("content");
        String explaination = data.getString("explaination");
        String crime = data.getString("crime");
        JSONArray keyword = data.getJSONArray("keyword");
        if(lawService.insert(lawtype,title,content,explaination,crime,keyword)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"can't insert");
        }
    }

    /**
     * 根据id删除法律
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(@RequestParam int id){
        if(lawService.deleteById(id)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"error id");
        }
    }

    /**
     *
     * @param
     * @return
     */
    //传进来的keyword是JSONArray,所以无法用Law去承接
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateById(@RequestBody JSONObject data){
        int id = data.getInteger("id");
        String lawtype = data.getString("lawtype");
        String title = data.getString("title");
        String content = data.getString("content");
        String explaination = data.getString("explaination");
        String crime = data.getString("crime");
        JSONArray keyword = data.getJSONArray("keyword");
        if(lawService.updateById(id,title,lawtype,content,explaination,crime,keyword)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"can't update");
        }
    }

    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,lawService.count());
    }
}
