package com.juity.blog.SERVICE.attach;

import com.juity.blog.DTO.attachDto;
import com.juity.blog.POJO.attach;
import com.github.pagehelper.PageInfo;

public interface attachService{
    PageInfo<attachDto> getAttaches(Integer page,Integer limit);
    void addAttach(attach attach);
    void deleteAttach(Integer id);
    attach getAttachById(Integer id);
}
