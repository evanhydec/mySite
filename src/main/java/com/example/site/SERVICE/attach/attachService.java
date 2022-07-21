package com.example.site.SERVICE.attach;

import com.example.site.DTO.attachDto;
import com.example.site.POJO.attach;
import com.github.pagehelper.PageInfo;

public interface attachService{
    PageInfo<attachDto> getAttaches(Integer page,Integer limit);
    void addAttach(attach attach);
    void deleteAttach(Integer id);
    attach getAttachById(Integer id);
}
