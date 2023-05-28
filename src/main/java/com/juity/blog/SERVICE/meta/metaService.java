package com.juity.blog.SERVICE.meta;

import com.juity.blog.DTO.cond.metaCond;
import com.juity.blog.DTO.metaDto;
import com.juity.blog.POJO.meta;

import java.util.List;


public interface metaService {
    List<metaDto> getMetaList(String type, int limit);
    void addMetas(Integer cid,String names,String type);
    List<meta> getMetas(metaCond metaCond);
    void updateMeta(meta meta);
    void addMeta(meta meta);
    void delMetaById(Integer mid);
    void saveMeta(String type, String name, Integer mid);
}
