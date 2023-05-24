package com.juity.blog.SERVICE.content;

import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.POJO.content;
import com.github.pagehelper.PageInfo;


public interface contentService {
    PageInfo<content> getArticlesByCond(contentCond contentCond,int page,int limit);
    void addArticle(content content);
    void updateArticle(content content);
    void delArticleById(Integer cid);
    content getArticleById(Integer cid);
    void updateCategory(String ordinal, String newCategory);
    void updateContentByCid(content content);
}
