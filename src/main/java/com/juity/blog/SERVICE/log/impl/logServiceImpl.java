package com.juity.blog.SERVICE.log.impl;

import com.juity.blog.DAO.logDao;
import com.juity.blog.POJO.log;
import com.juity.blog.SERVICE.log.logService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class logServiceImpl implements logService {
    @Autowired
    private logDao logDao;

    @Override
    public PageInfo<log> getLogs(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<log> logs = logDao.getLogs();
        PageInfo<log> pageInfo = new PageInfo<>(logs);
        return pageInfo;
    }

    @Override
    public void addLog(String action, String data, String ip, Integer authorId) {
        log log = new log();
        log.setAction(action);
        log.setAuthorId(authorId);
        log.setIp(ip);
        log.setData(data);
        logDao.addLog(log);
    }
}
