package com.juity.blog.DAO;

import com.juity.blog.DTO.cond.userCond;
import com.juity.blog.POJO.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface userDao {
    int updateUser(user user);
    user getUserById(@Param("uid")Integer id);
    user getUserByCond(userCond userCond);
}
