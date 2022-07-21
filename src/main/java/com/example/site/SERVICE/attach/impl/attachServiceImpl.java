package com.example.site.SERVICE.attach.impl;

import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.DAO.attachDao;
import com.example.site.DTO.attachDto;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.attach;
import com.example.site.SERVICE.attach.attachService;
import com.example.site.utils.qiniu;
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
