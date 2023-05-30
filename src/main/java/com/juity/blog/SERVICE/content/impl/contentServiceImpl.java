package com.juity.blog.SERVICE.content.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.DAO.commentDao;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.MongoWrapper;
import com.juity.blog.POJO.comment;
import com.juity.blog.POJO.content;
import com.juity.blog.SERVICE.content.contentService;
import com.juity.blog.SERVICE.meta.metaService;
import com.github.pagehelper.PageInfo;
import com.juity.blog.utils.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class contentServiceImpl implements contentService {
    @Lazy
    @Autowired
    private metaService metaService;
    @Autowired
    private commentDao commentDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageInfo<content> getArticlesByCond(content contentCond, int page, int limit) {
        if (contentCond == null) throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        Query query;
        try {
            query = new MongoWrapper(contentCond).buildQuery();
        } catch (IllegalAccessException e) {
            throw new BusinessException("build Query Exception");
        }
        int sum = (int) mongoTemplate.count(query, content.class);
        PageInfo<content> contentPageInfo = Tools.buildPage(page, limit, sum, content.class);
        int offset = limit * (page - 1);
        List<content> articles = mongoTemplate.find(query.with(Sort.by(Sort.Order.desc("create_time"))).skip(offset).limit(limit), content.class);
        contentPageInfo.setList(articles);
        return contentPageInfo;
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

        content.setCid(49);
        mongoTemplate.insert(content);

        //标签和分类
        int cid = content.getCid();
        String tags = content.getTags();
        String categories = content.getCategories();
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    public void updateArticle(content content) {
        String tags = content.getTags();
        String categories = content.getCategories();
        int cid = content.getCid();
        updateContentByCid(content);
        metaService.MinusMetas(cid);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    public void delArticleById(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        mongoTemplate.remove(new Query().addCriteria(Criteria.where("cid").is(cid)), content.class);
        metaService.MinusMetas(cid);
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
        return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("cid").is(cid)), content.class);
    }

    @Override
    @Transactional
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void updateCategory(String ordinal, String newCategory) {
        content cond = new content();
        cond.setCategories(ordinal);
        Query query;
        try {
            query = new MongoWrapper(cond).buildQuery();
        } catch (IllegalAccessException e) {
            throw new BusinessException("Build query Exception");
        }
        mongoTemplate.updateMulti(query, new Update().set("categories", newCategory), content.class);
    }

    @Override
    public void updateContentByCid(content content) {
        if (null != content && null != content.getCid()) {
            Update update;
            try {
                update = new MongoWrapper(content).buildUpdate();
            } catch (IllegalAccessException e) {
                throw new BusinessException("build Update Exception");
            }
            mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("cid").is(content.getCid())), update, content.getClass());
        }
    }

}
