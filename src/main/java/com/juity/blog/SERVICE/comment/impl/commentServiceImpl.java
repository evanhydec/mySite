package com.juity.blog.SERVICE.comment.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.DAO.commentDao;
import com.juity.blog.DTO.cond.commentCond;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.comment;
import com.juity.blog.POJO.content;
import com.juity.blog.SERVICE.comment.commentService;
import com.juity.blog.SERVICE.content.contentService;
import com.juity.blog.utils.DateKit;
import com.juity.blog.utils.TaleUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class commentServiceImpl implements commentService {
    @Autowired
    private commentDao commentDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private contentService contentService;
    private static final String STATUS_NORMAL = "approved";
    private static final String STATUS_BLANK = "not_audit";

    @Override
    public PageInfo<comment> getCommentsByCond(commentCond commentCond, Integer page, Integer limit) {
        if (null == commentCond)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(page, limit);
        List<comment> comments = commentDao.getCommentsByCond(commentCond);
        PageInfo<comment> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }

    @Override
    public comment getCommentById(Integer coId) {
        if (coId == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentDao.getCommentById(coId);
    }

    @Override
    public void deleteComment(Integer coId) {
        if (coId == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        content article = contentService.getArticleById(commentDao.getCommentById(coId).getCid());
        if (article != null) {
            mongoTemplate.updateFirst(
                    new Query().addCriteria(Criteria.where("cid").is(article.getCid())),
                    new Update().inc("comment_num", -1),
                    content.class);
            commentDao.delComment(coId);
        }
    }

    @Override
    public void updateCommentStatus(Integer coId, String status) {
        if (null == coId)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        commentDao.updateCommentStatus(coId, status);
    }

    @Override
    public List<comment> getCommentsById(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentDao.getCommentsByCid(cid);
    }

    @Override
    public void addComment(comment comment) {
        String msg = null;
        if (null == comment) {
            msg = "评论对象为空";
        }
        if (comment != null) {
            if (StringUtils.isBlank(comment.getAuthor())) {
                comment.setAuthor("热心网友");
            }
            if (StringUtils.isNotBlank(comment.getMail()) && !TaleUtils.isEmail(comment.getMail())) {
                msg = "请输入正确的邮箱格式";
            }
            if (StringUtils.isBlank(comment.getContent())) {
                msg = "评论内容不能为空";
            }
            if (comment.getContent().length() < 5 || comment.getContent().length() > 2000) {
                msg = "评论字数在5-2000个字符";
            }
            if (null == comment.getCid()) {
                msg = "评论文章不能为空";
            }
            if (msg != null)
                throw BusinessException.withErrorCode(msg);
            content article = contentService.getArticleById(comment.getCid());
            if (null == article)
                throw BusinessException.withErrorCode("该文章不存在");
            comment.setOwnerId(article.getAuthorId());
            comment.setStatus(STATUS_BLANK);
            comment.setCreated(DateKit.getCurrentUnixTime());
            commentDao.addComment(comment);

            content temp = new content();
            temp.setCid(article.getCid());
            temp.setCommentsNum(1);
            contentService.updateContentByCid(temp);
        }
    }
}
