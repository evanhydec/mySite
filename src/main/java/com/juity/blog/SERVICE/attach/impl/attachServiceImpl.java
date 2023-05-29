package com.juity.blog.SERVICE.attach.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.DAO.attachDao;
import com.juity.blog.DTO.attachDto;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.attach;
import com.juity.blog.SERVICE.attach.attachService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class attachServiceImpl implements attachService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageInfo<attach> getAttaches(Integer page, Integer limit) {
        PageInfo<attach> res = new PageInfo<>();
        res.setPageNum(1);
        res.setPageSize(limit);
        res.setPages(1);
        int sum = (int) mongoTemplate.count(new Query(), attach.class);
        int pages = (sum - 1) / limit + 1;
        if (sum == 0 || page > pages || page <= 0) {
            return res;
        }

        res.setPages(pages);
        int offset = limit * (page - 1);
        if (page > 1) {
            res.setHasPreviousPage(true);
            res.setPrePage(page - 1);
        }
        if (page < pages) {
            res.setHasNextPage(true);
            res.setNextPage(page + 1);
        }
        res.setNavigatepageNums(IntStream.rangeClosed(1, pages).toArray());
        List<attach> attaches = mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("created"))).skip(offset).limit(limit), attach.class);
        res.setList(attaches);
        return res;
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
