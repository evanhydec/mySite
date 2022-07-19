package com.example.site.SERVICE.content.impl;

import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.CONSTANT.Types;
import com.example.site.CONSTANT.webConst;
import com.example.site.DAO.commentDao;
import com.example.site.DAO.contentDao;
import com.example.site.DAO.relationshipDao;
import com.example.site.DTO.archiveDto;
import com.example.site.DTO.cond.contentCond;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.comment;
import com.example.site.POJO.content;
import com.example.site.SERVICE.content.contentService;
import com.example.site.SERVICE.meta.metaService;
import com.example.site.SERVICE.site.siteService;
import com.example.site.utils.DateKit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class contentServiceImpl implements contentService {
    @Autowired
    private contentDao contentDao;

    @Lazy
    @Autowired
    private metaService metaService;
    @Autowired
    private relationshipDao relationshipDao;
    @Autowired
    private commentDao commentDao;

    @Override
    public PageInfo<content> getArticlesByCond(contentCond contentCond, int page, int limit) {
        if (contentCond == null) throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(page,limit);
        List<content> articles = contentDao.getArticlesByCond(contentCond);
        PageInfo<content> res = new PageInfo<>(articles);
        return res;
    }

    @Override
    public void addArticle(content content) {
        if (null == content)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        if (StringUtils.isBlank(content.getTitle()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);
        if (content.getTitle().length() > webConst.MAX_TITLE_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);
        if (StringUtils.isBlank(content.getContent()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);
        if (content.getContent().length() > webConst.MAX_TEXT_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);

        //标签和分类
        String tags = content.getTags();
        String categories = content.getCategories();

        contentDao.addArticle(content);

        int cid = content.getCid();
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    public void updateArticle(content content) {
        String tags = content.getTags();
        String categories = content.getCategories();

        contentDao.updateArticle(content);
        int cid = content.getCid();
        relationshipDao.delRelationshipByCid(cid);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    public void delArticleById(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        contentDao.delArticleById(cid);
        relationshipDao.delRelationshipByCid(cid);
        List<comment> comments = commentDao.getCommentsByCid(cid);
        comments.forEach(
            comment -> {
                commentDao.delComment(comment.getCoId());
            }
        );
    }

    @Override
    public content getArticleById(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return contentDao.getArticleById(cid);
    }

    @Override
    @Transactional
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void updateCategory(String ordinal, String newCategory) {
        contentCond cond = new contentCond();
        cond.setCategory(ordinal);
        List<content> articles = contentDao.getArticlesByCond(cond);
        articles.forEach(article -> {
            article.setCategories(newCategory);
            contentDao.updateArticle(article);
        });
    }

    @Override
    public void updateContentByCid(content content) {
        if (null != content && null != content.getCid()) {
            contentDao.updateArticle(content);
        }
    }


}
