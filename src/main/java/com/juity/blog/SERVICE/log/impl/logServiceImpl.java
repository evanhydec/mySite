package com.juity.blog.SERVICE.log.impl;

import com.juity.blog.DAO.logDao;
import com.juity.blog.POJO.log;
import com.juity.blog.SERVICE.log.logService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Service
public class logServiceImpl implements logService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<log> getLogs(int pageNum, int pageSize) {
        int offset = pageSize * (pageNum - 1);
        Query query = new Query().with(Sort.by(Sort.Order.desc("created")))
                .skip(offset)
                .limit(pageSize);
        return mongoTemplate.find(query, log.class);
    }

    @Override
    public void addLog(String action, String data, String ip, Integer authorId) {
        log log = new log();
        log.setAction(action);
        log.setAuthorId(authorId);
        log.setIp(ip);
        log.setData(data);
        log.setCreated(new Date());
        mongoTemplate.insert(log);
    }
}
