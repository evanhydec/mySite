package com.juity.blog;

import com.juity.blog.DAO.attachDao;
import com.juity.blog.DAO.contentDao;
import com.juity.blog.DTO.attachDto;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.POJO.attach;
import com.juity.blog.POJO.content;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class moveDataTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private attachDao attachDao;

    @Autowired
    private contentDao contentDao;

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
                attach attach = new attach();
                BeanUtils.copyProperties(attachDto, attach, "created");
                attach.setCreated(new Date(attachDto.getCreated() * 1000L));
                attaches.add(attach);
        });
        mongoTemplate.insertAll(attaches);
    }

    @Test
    public void moveArticles() {
        boolean exist = mongoTemplate.collectionExists("article");
        if (exist) {
            mongoTemplate.dropCollection("article");
            mongoTemplate.createCollection("article");
        }
        List<content> articlesByCond = contentDao.getArticlesByCond(new contentCond());
        articlesByCond.forEach(article-> {
            article.setAuthorId(1);
            article.setAuthorName("evanhydec");
            article.setUpdateTime(new Date(article.getModified() * 1000L));
            article.setCreateTime(new Date(article.getCreated() * 1000L));
            article.setCreated(null);
            article.setModified(null);
        });
        mongoTemplate.insertAll(articlesByCond);
    }
}
