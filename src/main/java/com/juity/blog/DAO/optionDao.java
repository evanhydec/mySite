package com.juity.blog.DAO;

import com.juity.blog.POJO.option;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface optionDao {
    int delOptionByName(@Param("name")String name);
    int updateOption(option option);
    option getOptionByName(@Param("name") String name);
    List<option> getOptions();
}
