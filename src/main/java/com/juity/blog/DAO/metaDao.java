package com.juity.blog.DAO;

import com.juity.blog.DTO.cond.metaCond;
import com.juity.blog.DTO.metaDto;
import com.juity.blog.POJO.meta;
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
