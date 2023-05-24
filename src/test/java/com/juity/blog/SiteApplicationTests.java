package com.juity.blog;

import com.juity.blog.DAO.relationshipDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SiteApplicationTests {
    @Autowired
    relationshipDao relationshipDao;
    @Test
    void contextLoads() {
        System.out.println(relationshipDao.delRelationshipByCid(1));
    }

}
