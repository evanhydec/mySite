package com.juity.blog;

import com.juity.blog.DAO.logDao;
import com.juity.blog.POJO.log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;

@SpringBootTest
@Slf4j
public class moveDataTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private logDao logDao;

    @Test
    public void moveLogs() {
        boolean exist = mongoTemplate.collectionExists("log");
        if (exist) {
            mongoTemplate.dropCollection("log");
            mongoTemplate.createCollection("log");
        }
        log.info("execute end...");
    }
}
