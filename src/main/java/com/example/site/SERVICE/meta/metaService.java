package com.example.site.SERVICE.meta;

import com.example.site.DTO.cond.metaCond;
import com.example.site.DTO.metaDto;
import com.example.site.POJO.meta;

import java.util.List;


public interface metaService {
    List<metaDto> getMetaList(String type, String orderBy, int limit);
    void addMetas(Integer cid,String names,String type);
    List<meta> getMetas(metaCond metaCond);
    void updateMeta(meta meta);
    void addMeta(meta meta);
    void delMetaById(Integer mid);
    void saveMeta(String type, String name, Integer mid);
}
