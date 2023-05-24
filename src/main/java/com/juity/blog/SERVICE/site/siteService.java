package com.juity.blog.SERVICE.site;

import com.juity.blog.DTO.archiveDto;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.DTO.statisticDto;
import com.juity.blog.POJO.comment;
import com.juity.blog.POJO.content;

import java.util.List;

public interface siteService {
    List<comment> getComments(int limit);
    List<content> getNewArticles(int limit);
    statisticDto getStatistics();
    List<archiveDto> getArchives(contentCond contentCond);
}
