package com.example.site.SERVICE.site.impl;

import com.example.site.CONSTANT.Types;
import com.example.site.DAO.attachDao;
import com.example.site.DAO.commentDao;
import com.example.site.DAO.contentDao;
import com.example.site.DAO.metaDao;
import com.example.site.DTO.archiveDto;
import com.example.site.DTO.cond.commentCond;
import com.example.site.DTO.cond.contentCond;
import com.example.site.DTO.statisticDto;
import com.example.site.POJO.comment;
import com.example.site.POJO.content;
import com.example.site.SERVICE.site.siteService;
import com.example.site.utils.DateKit;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class siteServiceImpl implements siteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(siteServiceImpl.class);
    @Autowired
    private commentDao commentDao;
    @Autowired
    private contentDao contentDao;
    @Autowired
    private metaDao metaDao;
    @Autowired
    private attachDao attachDao;
    @Override
    public List<comment> getComments(int limit) {
        LOGGER.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10){
            limit = 10;
        }
        PageHelper.startPage(1, limit);
        List<comment> rs = commentDao.getCommentsByCond(new commentCond());
        LOGGER.debug("Exit recentComments method");
        return rs;
    }

    @Override
    public List<content> getNewArticles(int limit) {
        LOGGER.debug("Enter recentArticles method:limit={}", limit);
        if (limit < 0 || limit > 10) limit = 10;
        PageHelper.startPage(1, limit);
        List<content> rs = contentDao.getArticlesByCond(new contentCond());
        LOGGER.debug("Exit recentArticles method");
        return rs;
    }

    @Override
    public statisticDto getStatistics() {
        LOGGER.debug("Enter recentStatistics method");
        //文章总数
        Long artices = contentDao.getArticleCount();
        Long comments = commentDao.getCommentsCount();
        Long links = metaDao.getMetasCountByType(Types.LINK.getType());
        Long atts = attachDao.getAttachesCount();
        statisticDto rs = new statisticDto();
        rs.setArticles(artices);
        rs.setAttaches(atts);
        rs.setComments(comments);
        rs.setLinks(links);
        LOGGER.debug("Exit recentStatistics method");
        return rs;
    }

    private void parseArchives(List<archiveDto> archives, contentCond contentCond) {
        if (null != archives){
            archives.forEach(archive -> {
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                contentCond cond = new contentCond();
                cond.setStartTime(start);
                cond.setEndTime(end);
                cond.setType(contentCond.getType());
                List<content> contents = contentDao.getArticlesByCond(cond);
                archive.setArticles(contents);
            });
        }
    }

    @Override
    @Cacheable(value = "siteCache", key = "'archives_' + #p0")
    public List<archiveDto> getArchives(contentCond contentCond) {
        LOGGER.debug("Enter getArchives method");
        List<archiveDto> archives = contentDao.getArchive(contentCond);
        parseArchives(archives, contentCond);
        LOGGER.debug("Exit getArchives method");
        return archives;
    }
}
