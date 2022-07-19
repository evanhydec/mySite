package com.example.site.SERVICE.attach.impl;

import com.example.site.DAO.attachDao;
import com.example.site.DTO.attachDto;
import com.example.site.POJO.attach;
import com.example.site.SERVICE.attach.attachService;
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
}
