package com.lyun.policelearning.controller.law;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.Law;
import com.lyun.policelearning.service.LawService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/law/manage")
public class LawManageApi {
    @Autowired
    LawService lawService;

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;
    /**
     * 获取所有的法律类型
     * @return 返回法律类型这一列表
     */
    @RequestMapping("")
    public Object getLawType(){
        return new ResultBody<>(true,200,lawService.findAllType());
    }


    /**
     * 根据法律的类型查询对应的title有哪些
     * @param lawtype
     * @return 返回对应的title目录
     */
    @RequestMapping(value = "/catalogue",method = RequestMethod.GET)
    public Object getCatalogueByType(@RequestParam String lawtype, HttpServletRequest request){
        if (!UserUtils.checkPower(request, 5,jwtConfig, userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
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
        if(lawService.updateById(id,lawtype,title,content,explaination,crime,keyword)){
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,500,"can't update");
        }
    }
}
