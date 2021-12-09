package com.lyun.policelearning.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyun.policelearning.dao.InformationDao;
import com.lyun.policelearning.entity.Information;
import com.lyun.policelearning.entity.Rule;
import com.lyun.policelearning.utils.page.PageRequest;
import com.lyun.policelearning.utils.page.PageResult;
import com.lyun.policelearning.utils.page.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InformationServiceImpl implements InformationService{

    @Autowired
    private InformationDao informationDao;

    @Override
    public List<JSONObject> findAll() {
        List<JSONObject> informationList = new ArrayList<>();
        //先将istop为true的数据放进去
        for(Information information : informationDao.findTop()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            jsonObject.put("picture",information.getPicture());
            informationList.add(jsonObject);
        }
        for(Information information : informationDao.findNotTop()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",information.getId());
            jsonObject.put("title",information.getTitle());
            jsonObject.put("view",information.getView());
            jsonObject.put("date",information.getDate());
            jsonObject.put("istop",information.getIstop());
            jsonObject.put("picture",information.getPicture());
            informationList.add(jsonObject);
        }
        return informationList;
    }

    @Override
    public JSONObject getInformationById(int id) {
        JSONObject information = new JSONObject();
        String content = informationDao.getInformationById(id).getContent();
        content = content.replace("\n","<br>");
        information.put("title",informationDao.getInformationById(id).getTitle());
        information.put("content",content);
        information.put("date",informationDao.getInformationById(id).getDate());
        information.put("view",informationDao.getInformationById(id).getView());
        information.put("picture",informationDao.getInformationById(id).getPicture());
        return information;
    }

    /**  根据pageRequest中的当前页、每页的大小，返回自定义page类型的队形
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult findPage(PageRequest pageRequest) {
        //将pageInfo传递到getPageResult中获得result结果，而pageInfo又是与数据库中的数据有关的
        return PageUtil.getPageResult(getPageInfo(pageRequest));
    }

    @Override
    public boolean insertInformation(Information information,String path) {
        if (information == null || path == null){
            return false;
        }
        informationDao.insertInformation(information,path);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        if(id <= 0){
            return false;
        }else {
            informationDao.deleteById(id);
            return true;
        }
    }

    @Override
    public void updateById( Information information) {
            informationDao.updateById(information);
    }

    @Override
    public boolean updateTopById(int id, int istop) {
       informationDao.updateTopById(id,istop);
       return true;
    }

    @Override
    public void updateView(int id) {
        informationDao.updateView(id);
    }

    private PageInfo<?> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //设置分页数据
        PageHelper.startPage(pageNum,pageSize);
        List<Information> informationList = informationDao.selectPage();
        return new PageInfo<>(informationList);
    }

}
