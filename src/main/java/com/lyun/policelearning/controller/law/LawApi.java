package com.lyun.policelearning.controller.law;

import com.alibaba.fastjson.JSONObject;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.service.CollectService;
import com.lyun.policelearning.service.LawService;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/law")
public class LawApi {
    @Autowired
    LawService lawService;
    @Autowired
    CollectService collectService;
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
     * 用作测试
     * @return 返回lawtype表中的所有数据
     */
    @RequestMapping("/all")
    public Object getAllLawType(){
        return new ResultBody<>(true,200,lawService.findAll());
    }
    /**
     * 根据法律的类型查询对应的title有哪些
     * @param lawtype
     * @return 返回对应的title目录
     */
    @RequestMapping(value = "/catalogue",method = RequestMethod.GET)
    public Object getCatalogueByType(@RequestParam String lawtype){
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
        public Object getContent(@RequestParam String title, HttpServletRequest request) {
        if (title == null) {
            return new ResultBody<>(false, 500, "error title");
        }
        int userId = UserUtils.getUserId(request,jwtConfig);
        JSONObject res = lawService.findContent(title);
        res.put("isCollect",collectService.isCollect(2,res.getInteger("id"),userId));
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

}
