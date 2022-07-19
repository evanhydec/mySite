package com.example.site.SERVICE.comment.impl;

import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.DAO.commentDao;
import com.example.site.DTO.cond.commentCond;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.comment;
import com.example.site.SERVICE.comment.commentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class commentServiceImpl implements commentService {
    @Autowired
    private commentDao commentDao;

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
        commentDao.delComment(coId);
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
}
