package com.juity.blog;

import com.juity.blog.DAO.attachDao;
import com.juity.blog.DAO.logDao;
import com.juity.blog.DTO.attachDto;
import com.juity.blog.POJO.attach;
import com.juity.blog.POJO.log;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class moveDataTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private attachDao attachDao;

    @Test
    public void moveLogs() {
        boolean exist = mongoTemplate.collectionExists("log");
        if (exist) {
            mongoTemplate.dropCollection("log");
            mongoTemplate.createCollection("log");
        }
        log.info("execute end...");
    }

    @Test
    public void moveAttaches() {
        boolean exist = mongoTemplate.collectionExists("attach");
        if (exist) {
            mongoTemplate.dropCollection("attach");
            mongoTemplate.createCollection("attach");
        }
        List<attachDto> attachDtos = attachDao.getAttaches();
        List<attach> attaches = new ArrayList<>();
        attachDtos.forEach(attachDto -> {
            for (int i = 0; i < 20; i++) {
                attach attach = new attach();
                BeanUtils.copyProperties(attachDto, attach, "created");
                attach.setId(i);
                attach.setCreated(new Date(attachDto.getCreated() * 1000L));
                attaches.add(attach);
            }
        });
        mongoTemplate.insertAll(attaches);
    }

    @Test
    public void testRemove() {
        attach attach = mongoTemplate.findAndRemove(new Query().addCriteria(Criteria.where("aid").is(13)), attach.class);
        System.out.println(attach);
    }
}
