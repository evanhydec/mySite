package com.example.site.SERVICE.log;

import com.example.site.POJO.log;
import com.github.pagehelper.PageInfo;

public interface logService {
    PageInfo<log> getLogs(int pageNum, int pageSize);
    void addLog(String action, String data, String ip, Integer authorId);
}
