package com.juity.blog.SERVICE.comment;

import com.juity.blog.DTO.cond.commentCond;
import com.juity.blog.POJO.comment;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface commentService {
    PageInfo<comment> getCommentsByCond(commentCond commentCond,Integer page,Integer limit);
    comment getCommentById(Integer coId);
    void deleteComment(Integer coId);
    void updateCommentStatus(Integer coId,String status);
    List<comment> getCommentsById(Integer cid);
    void addComment(comment comment);
}
