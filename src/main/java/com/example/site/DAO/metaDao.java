package com.example.site.DAO;

import com.example.site.DTO.cond.metaCond;
import com.example.site.DTO.metaDto;
import com.example.site.POJO.meta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface metaDao {
    int addMeta(meta meta);
    int delMetaById(@Param("mid")Integer mid);
    int updateMeta(meta meta);
    meta getMetaById(@Param("mid")Integer mid);
    List<meta> getMetaByCond(metaCond metacond);
    Long getMetasCountByType(@Param("type")String type);
    List<metaDto> selectFromSql(Map<String, Object> paraMap);
}
