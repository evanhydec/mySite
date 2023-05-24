package com.juity.blog.DAO;

import com.juity.blog.DTO.attachDto;
import com.juity.blog.POJO.attach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface attachDao {
    int addAttach(attach attAchDomain);
    int batchAddAttach(List<attach> list);

    int delAttach(int id);

    int updateAttach(attach attach);

    attach getAttachById(@Param("id") int id);

    List<attachDto> getAttaches();

    Long getAttachesCount();
}
