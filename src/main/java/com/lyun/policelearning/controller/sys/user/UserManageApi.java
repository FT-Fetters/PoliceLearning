package com.lyun.policelearning.controller.sys.user;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.POJONode;
import com.lyun.policelearning.annotation.Permission;
import com.lyun.policelearning.config.JwtConfig;
import com.lyun.policelearning.entity.User;
import com.lyun.policelearning.entity.UserTemplate;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.service.DeptService;
import com.lyun.policelearning.service.RoleService;
import com.lyun.policelearning.service.UserService;
import com.lyun.policelearning.utils.PinYinUtil;
import com.lyun.policelearning.utils.ResultBody;
import com.lyun.policelearning.utils.UserUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Permission
@RestController
@RequestMapping("/user/manage")
public class UserManageApi {

    @Autowired
    UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    RoleService roleService;

    @Autowired
    DeptService deptService;

    @Permission(admin = true)
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public Object count(){
        return new ResultBody<>(true,200,userService.count());
    }

    @Permission(admin = true)
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public Object newUser(@RequestBody JSONObject data){
        String username = data.getString("username");
        String password = data.getString("password");
        String nickname = data.getString("nickname");
        String realname = data.getString("realname");
        Integer role = data.getInteger("role");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        Long dept = data.getLong("dept");
        if (username == null || password == null || role == null || sex == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        if (userService.getByUsername(username) == null){
            userService.newUser(username,password,nickname,realname,role,phone,sex,dept);
            return new ResultBody<>(true,200,null);
        }else {
            return new ResultBody<>(false,501,"username is exists");
        }
    }

    @Permission(admin = true)
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteUser(@RequestBody JSONObject data, HttpServletRequest request){
        Integer id = data.getInteger("id");
        if (id == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        userService.deleteUser(id);
        return new ResultBody<>(true,200,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateUser(@RequestBody JSONObject data){
        Integer id = data.getInteger("id");
        String username = data.getString("username");
        String nickname = data.getString("nickname");
        String realname = data.getString("realname");
        Integer role = data.getInteger("role");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        Long dept = data.getLong("dept");
        if (username == null || role == null || sex == null){
            return new ResultBody<>(false,500,"miss parameter");
        }
        userService.updateUser(id,username,nickname,realname,role,phone,sex,dept);
        return new ResultBody<>(true,200,null);
    }

    @Permission(admin = true)
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object getAll(){
        return new ResultBody<>(true,200,userService.findAll());
    }

    @RequestMapping(value = "/update/nickname",method = RequestMethod.POST)
    public Object updateNickname(@RequestBody JSONObject data,HttpServletRequest request){
        if (!UserUtils.getUsername(request, jwtConfig).equals(data.getString("username"))){
            return new ResultBody<>(false,-1,"not allow");
        }
        userService.changeNickname(data.getString("username"),data.getString("nickname"));
        return new ResultBody<>(true,200,null);

    }


    @RequestMapping(value = "/change/password",method = RequestMethod.POST)
    public Object changePassword(@RequestBody JSONObject data){
        String username = data.getString("username");
        String password = data.getString("password");
        if (username == null || password == null)return new ResultBody<>(false,500,"miss parameter");
        if (userService.getByUsername(username)==null)return new ResultBody<>(false,501,"unknown username");
        userService.changePassword(username,password);
        return new ResultBody<>(true,200,null);
    }

    @Permission(admin = true)
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public Object importCop(@RequestBody JSONObject data){
        String name = data.getString("name");
        String phone = data.getString("phone");
        String sex = data.getString("sex");
        Long dept = data.getLong("dept");
        if (name == null || phone == null || sex == null)return new ResultBody<>(false,500,"miss parameter");
        if (name.length() > 4)return new ResultBody<>(false,501,"name is too long");
        String username = PinYinUtil.getPinyin(name) + new Random().nextInt(3000);
        String password = DigestUtils.md5DigestAsHex((username + phone + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8)).substring(0,10);
        userService.newUser(username,password,name,name,2,phone,sex,dept);
        JSONObject ret = new JSONObject();
        ret.put("username",username);
        ret.put("password",password);
        return new ResultBody<>(true,200,ret);
    }
    /**
     * 2022.8.13
     * deng
     * 获取警员模板
     */
    @SneakyThrows
    @RequestMapping(value = "/download/template",method = RequestMethod.GET)
    public void getTemplate(HttpServletResponse response){
        List<UserTemplate> users = new ArrayList<>();
        UserTemplate userTemplate = new UserTemplate();
        userTemplate.setName("张三");
        userTemplate.setPhone("150xxxxxxxx");
        userTemplate.setSex("男");
        userTemplate.setDept("通讯部");
        users.add(userTemplate);
        //设置头属性  设置文件名称
        response.setHeader("Content-Disposition", "attachment;filename=template.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(UserTemplate.class)
                .sheet("模板")
                .doWrite(users);
    }
    /**
     * 2022.8.13
     * deng
     * 警员批量导入
     */
    @SneakyThrows
    @RequestMapping(value = "/import/excel",method = RequestMethod.POST)
    public Object importExcel(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return new ResultBody<>(true,-1,"empty file");
        }
        List<UserTemplate> userTemplates = EasyExcel.read(file.getInputStream()).head(UserTemplate.class).sheet().doReadSync();
        for(UserTemplate userTemplate : userTemplates){
            String username = PinYinUtil.getPinyin(userTemplate.getName()) + new Random().nextInt(3000);
            String password = "123456";
            Long dept = 0L;
            if (deptService.getByName(userTemplate.getDept()) != null){
                 dept = Long.parseLong(deptService.getByName(userTemplate.getDept()).getId());
            }
            userService.newUser(username, password, userTemplate.getName(), userTemplate.getName(), 2,userTemplate.getPhone(),userTemplate.getSex(),dept);
        }
        return new ResultBody<>(true,200,null);
    }
    /**
     * 2022.8.13
     * deng
     * 批量导出警员
     */
    @SneakyThrows
    @RequestMapping(value = "/output/excel",method = RequestMethod.GET)
    public void output(HttpServletResponse response){
        List<User> users = userService.findAll();
        for (User user : users){
            if ("e10adc3949ba59abbe56e057f20f883e".equals(user.getPassword())){
                user.setPassword("123456");
            }else {
                user.setPassword(null);
            }
            if (user.getDept() != null) {
                if (deptService.selectDeptById(user.getDept().toString()) != null) {
                    user.setDeptName(deptService.selectDeptById(user.getDept().toString()).getName());
                } else {
                    user.setDeptName(null);
                }
            }
        }
        response.setHeader("Content-Disposition", "attachment;filename=police.xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(User.class)
                .sheet("警员信息")
                .doWrite(users);
    }
}
