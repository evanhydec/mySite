package com.juity.blog.SERVICE.site.impl;

import com.juity.blog.CONSTANT.Types;
import com.juity.blog.DAO.commentDao;
import com.juity.blog.DAO.metaDao;
import com.juity.blog.DTO.cond.commentCond;
import com.juity.blog.DTO.statisticDto;
import com.juity.blog.POJO.attach;
import com.juity.blog.POJO.comment;
import com.juity.blog.POJO.content;
import com.juity.blog.SERVICE.site.siteService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class siteServiceImpl implements siteService {
    @Autowired
    private commentDao commentDao;
    @Autowired
    private metaDao metaDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<comment> getComments(int limit) {
        if (limit < 0 || limit > 10){
            limit = 10;
        }
        PageHelper.startPage(1, limit);
        List<comment> rs = commentDao.getCommentsByCond(new commentCond());
        return rs;
    }

    @Override
    public List<content> getNewArticles(int limit) {
        if (limit < 0 || limit > 10) limit = 10;
        return mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("create_time"))).limit(limit), content.class);
    }

    @Override
    public statisticDto getStatistics() {
        //文章总数
        Long articles = mongoTemplate.count(new Query(), content.class);
        Long comments = commentDao.getCommentsCount();
        Long links = metaDao.getMetasCountByType(Types.LINK.getType());
        long attaches = mongoTemplate.count(new Query(), attach.class);
        statisticDto rs = new statisticDto();
        rs.setArticles(articles);
        rs.setAttaches(attaches);
        rs.setComments(comments);
        rs.setLinks(links);
        return rs;
    }

//    private void parseArchives(List<archiveDto> archives, contentCond contentCond) {
//        if (null != archives){
//            archives.forEach(archive -> {
//                String date = archive.getDate();
//                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
//                int start = DateKit.getUnixTimeByDate(sd);
//                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
//                contentCond cond = new contentCond();
//                cond.setStartTime(start);
//                cond.setEndTime(end);
//                cond.setType(contentCond.getType());
//                List<content> contents = contentDao.getArticlesByCond(cond);
//                archive.setArticles(contents);
//            });
//        }
//    }

//    @Override
//    @Cacheable(value = "siteCache", key = "'archives_' + #p0")
//    public List<archiveDto> getArchives(contentCond contentCond) {
//        List<archiveDto> archives = contentDao.getArchive(contentCond);
//        parseArchives(archives, contentCond);
//        return archives;
//    }
}
