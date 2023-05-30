package com.juity.blog.SERVICE.attach.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.attach;
import com.juity.blog.SERVICE.attach.attachService;
import com.github.pagehelper.PageInfo;
import com.juity.blog.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class attachServiceImpl implements attachService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageInfo<attach> getAttaches(Integer page, Integer limit) {
        int sum = (int) mongoTemplate.count(new Query(), attach.class);
        PageInfo<attach> attachPageInfo = Tools.buildPage(page, limit, sum, attach.class);

        int offset = limit * (page - 1);
        List<attach> attaches = mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("created"))).skip(offset).limit(limit), attach.class);
        attachPageInfo.setList(attaches);
        return attachPageInfo;
    }

    @Override
    public void addAttach(attach attach) {
        if (null == attach)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attach.setCreated(new Date());
        mongoTemplate.insert(attach);
    }

    @Override
    public attach deleteAttach(Integer id) {
        if (null == id)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return mongoTemplate.findAndRemove(new Query().addCriteria(Criteria.where("aid").is(id)), attach.class);
    }
}
