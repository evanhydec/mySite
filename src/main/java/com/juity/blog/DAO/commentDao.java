package com.juity.blog.DAO;

import com.juity.blog.DTO.cond.commentCond;
import com.juity.blog.POJO.comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface commentDao {
    int addComment(comment comment);

    int delComment(@Param("coId") Integer coId);

    int updateCommentStatus(@Param("coId") Integer coId, @Param("status") String status);

    comment getCommentById(@Param("coId") Integer coId);

    List<comment> getCommentsByCid(@Param("cid") Integer cid);

    List<comment> getCommentsByCond(commentCond commentCond);

    Long getCommentsCount();
}
