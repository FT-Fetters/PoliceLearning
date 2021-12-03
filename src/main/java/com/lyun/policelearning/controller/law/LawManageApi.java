package com.lyun.policelearning.controller.law;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.service.LawService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/law/manage")
public class LawManageApi {
    @Autowired
    LawService lawService;

    @Autowired
    UserService userService;
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
        if (!UserUtils.checkPower(request,5,userService)){
            return new ResultBody<>(false,-1,"not allow");
        }
        if(lawtype == null){
            return new ResultBody<>(false,500,"error type");
        }
        JSONObject res = lawService.findTitleByName(lawtype);
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
}
