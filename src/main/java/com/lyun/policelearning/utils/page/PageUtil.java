package com.lyun.policelearning.utils.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PageUtil {
    /**
     * 将分页信息封装到统一的接口
     * @param
     * @param pageInfo
     * @return
     */
    public static PageResult getPageResult(PageInfo<?> pageInfo, Page page){
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page.getPageNum());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalSize(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }



    public static PageResult getPage(PageRequest pageRequest, List<?> list){
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageRequest.getPageNum());
        pageResult.setPageSize(pageRequest.getPageSize());
        pageResult.setTotalPages(
                        list.size() % pageResult.getPageSize() == 0 ?
                        list.size() / pageRequest.getPageSize() :
                        list.size() / pageRequest.getPageSize() + 1
                );
        pageResult.setTotalSize(list.size());
        if ((pageRequest.getPageNum() - 1)*pageRequest.getPageSize() > list.size()){
            list.clear();
            pageResult.setContent(list);
            return pageResult;
        }
        if (pageRequest.getPageSize() > list.size() && pageRequest.getPageNum() == 1){
            pageResult.setContent(list);
            return pageResult;
        }
        list = list.subList(
                Math.min((pageRequest.getPageNum() - 1) * pageRequest.getPageSize(), list.size() - pageRequest.getPageSize()),
                Math.min(pageRequest.getPageNum() * pageRequest.getPageSize(), list.size() - 1));
        pageResult.setContent(list);
        return pageResult;
    }
}
