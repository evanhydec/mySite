package com.example.site.SERVICE.attach;

import com.example.site.DTO.attachDto;
import com.github.pagehelper.PageInfo;

public interface attachService{
    PageInfo<attachDto> getAttaches(Integer page,Integer limit);
}
