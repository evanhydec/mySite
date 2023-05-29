package com.juity.blog.SERVICE.log;

import com.juity.blog.POJO.log;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface logService {
    List<log> getLogs(int pageNum, int pageSize);
    void addLog(String action, String data, String ip, Integer authorId);
}
