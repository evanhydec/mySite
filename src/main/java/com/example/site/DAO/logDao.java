package com.example.site.DAO;

import com.example.site.POJO.log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface logDao {
    int addLog(log log);

    int delLogById(@Param("id") Integer id);

    List<log> getLogs();
}
