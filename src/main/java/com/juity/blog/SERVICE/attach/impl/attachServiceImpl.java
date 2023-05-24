package com.juity.blog.SERVICE.attach.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.DAO.attachDao;
import com.juity.blog.DTO.attachDto;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.attach;
import com.juity.blog.SERVICE.attach.attachService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class attachServiceImpl implements attachService {
    @Autowired
    private attachDao attachDao;


    @Override
    public PageInfo<attachDto> getAttaches(Integer page, Integer limit) {
        List<attachDto> attaches = attachDao.getAttaches();
        PageHelper.startPage(page,limit);
        PageInfo<attachDto> attachPageInfo = new PageInfo<>(attaches);
        return attachPageInfo;
    }

    @Override
    public void addAttach(attach attach) {
        if (null == attach)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attachDao.addAttach(attach);
    }

    @Override
    public void deleteAttach(Integer id) {
        if (null == id)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attachDao.delAttach(id);
    }

    @Override
    public attach getAttachById(Integer id) {
        if (null == id)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return attachDao.getAttachById(id);
    }
}
