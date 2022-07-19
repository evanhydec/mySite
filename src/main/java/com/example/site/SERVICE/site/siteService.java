package com.example.site.SERVICE.site;

import com.example.site.DTO.archiveDto;
import com.example.site.DTO.cond.contentCond;
import com.example.site.DTO.statisticDto;
import com.example.site.POJO.comment;
import com.example.site.POJO.content;

import java.util.List;

public interface siteService {
    List<comment> getComments(int limit);
    List<content> getNewArticles(int limit);
    statisticDto getStatistics();
    List<archiveDto> getArchives(contentCond contentCond);
}
