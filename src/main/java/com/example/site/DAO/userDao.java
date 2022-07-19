package com.example.site.DAO;

import com.example.site.DTO.cond.userCond;
import com.example.site.POJO.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface userDao {
    int updateUser(user user);
    user getUserById(@Param("uid")Integer id);
    user getUserByCond(userCond userCond);
}
