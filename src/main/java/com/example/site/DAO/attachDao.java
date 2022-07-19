package com.example.site.DAO;

import com.example.site.DTO.attachDto;
import com.example.site.POJO.attach;
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
