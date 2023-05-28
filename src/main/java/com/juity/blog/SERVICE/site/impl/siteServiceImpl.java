package com.juity.blog.SERVICE.site.impl;

import com.juity.blog.CONSTANT.Types;
import com.juity.blog.DAO.attachDao;
import com.juity.blog.DAO.commentDao;
import com.juity.blog.DAO.contentDao;
import com.juity.blog.DAO.metaDao;
import com.juity.blog.DTO.archiveDto;
import com.juity.blog.DTO.cond.commentCond;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.DTO.statisticDto;
import com.juity.blog.POJO.comment;
import com.juity.blog.POJO.content;
import com.juity.blog.SERVICE.site.siteService;
import com.juity.blog.utils.DateKit;
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
        PageHelper.startPage(1, limit);
        List<content> rs = contentDao.getArticlesByCond(new contentCond());
        return rs;
    }

    @Override
    public statisticDto getStatistics() {
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
        List<archiveDto> archives = contentDao.getArchive(contentCond);
        parseArchives(archives, contentCond);
        return archives;
    }
}
