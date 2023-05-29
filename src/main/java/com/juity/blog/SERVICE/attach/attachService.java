package com.juity.blog.SERVICE.attach;

import com.juity.blog.DTO.attachDto;
import com.juity.blog.POJO.attach;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface attachService{
    PageInfo<attach> getAttaches(Integer page, Integer limit);
    void addAttach(attach attach);
    attach deleteAttach(Integer id);
}
