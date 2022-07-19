package com.example.site.SERVICE.content;

import com.example.site.DTO.cond.contentCond;
import com.example.site.POJO.content;
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
