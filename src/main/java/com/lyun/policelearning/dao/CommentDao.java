package com.lyun.policelearning.dao;

import com.lyun.policelearning.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface CommentDao extends BaseDao<Comment>{
    void comment(int userId, Date date,String content,String type,int hostId);
    void secondComment(int userId,int parentId,Date date,String content,String type,int hostId);
    List<Comment> findComment(int hostId,String type);
    List<Comment> findSecondComment(int hostId,int parentId,String type);
}
