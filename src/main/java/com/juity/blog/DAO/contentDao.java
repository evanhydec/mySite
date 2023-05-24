package com.juity.blog.DAO;

import com.juity.blog.DTO.archiveDto;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.POJO.content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface contentDao {
    int addArticle(content content);

    int delArticleById(@Param("cid") Integer cid);

    int updateArticle(content content);

    //文章评论数更新
    int updateArticleCommentCountById(@Param("cid") Integer cid, @Param("commentsNum") Integer commentsNum);

    content getArticleById(@Param("cid") Integer cid);

    List<content> getArticlesByCond(contentCond contentCond);

    Long getArticleCount();

    List<archiveDto> getArchive(contentCond contentCond);

    List<content> getRecentlyArticle();

    List<content> searchArticle(@Param("param") String param);
}
